package com.example.pureplate.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pureplate.FoodDetailsActivity
import com.example.pureplate.R
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FoodsListAdapter(
    private val context: Context, // Receive context from activity
    private val listener: (HashMap<String, Any>) -> Unit,
    private var selectedCategory: String // Receive selectedCategory from activity
) : RecyclerView.Adapter<FoodsListAdapter.Holder>() {

    private val foods = mutableListOf<HashMap<String, Any>>()

    // Get food data from Firestore on creation using initializer block
    init {
        val db = FirebaseFirestore.getInstance()
        val foodCollection = db.collection("foods") // Replace "foods" with your actual collection name

        foodCollection.get()
            .addOnSuccessListener { querySnapshot ->
                var query: Query = querySnapshot.query  // Access the Query object from QuerySnapshot

                if (selectedCategory.isNotEmpty()) {
                    query = query.whereEqualTo("category", selectedCategory) // Filter by category if selected
                }

                // Use addSnapshotListener for real-time data updates (optional)
                query.addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.w("Firestore", "Error getting foods: $exception")
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        for (documentChange in snapshot.documentChanges) {
                            val foodData = documentChange.document.data!!  // Access document data directly
                            when (documentChange.type) {
                                DocumentChange.Type.ADDED -> foods.add(foodData as HashMap<String, Any>)
                                DocumentChange.Type.MODIFIED -> {
                                    val index = foods.indexOfFirst { it["id"] == foodData["id"] }
                                    if (index != -1) {
                                        foods[index] = foodData as HashMap<String, Any>
                                    }
                                }
                                DocumentChange.Type.REMOVED -> {
                                    val index = foods.indexOfFirst { it["id"] == foodData["id"] }
                                    if (index != -1) {
                                        foods.removeAt(index)
                                    }
                                }
                            }
                        }
                        notifyDataSetChanged() // Notify adapter of data changes
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting foods: $exception")
            }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoodImage: ImageView = itemView.findViewById(R.id.imgFood)
        val tvFoodName: TextView = itemView.findViewById(R.id.txtFoodName)
        var tvRating: TextView = itemView.findViewById(R.id.txtFoodRating)

        fun bind(foodData: HashMap<String, Any>) {
            val name = foodData["name"] as String
            val imageUrl = when (name) {
                "Chicken Sub" -> R.drawable.chickensub_protein
                "Classic Salad" -> R.drawable.salad_healthy
                "Sushi Platter" -> R.drawable.sushi_protein
                else -> ""  // Optional default image path if the name doesn't match any case
            }
            tvFoodName.text = name

            // Load image from imageUrl using Glide
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(ivFoodImage)

            val rating = foodData["rating"] as String
            tvRating.text = rating
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_food_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val foodData = foods[position]
        holder.bind(foodData)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodDetailsActivity::class.java)
            intent.putExtra("foodData", foodData) // Pass car data to the activity
            holder.itemView.context.startActivity(intent)
        }
    }
}
