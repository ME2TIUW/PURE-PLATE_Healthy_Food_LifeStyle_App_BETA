package com.example.pureplate

import Transaction
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pureplate.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

var quantity = 1
val TRANSACTION_ADDED = "transaction_added"

class FoodDetailsActivity : AppCompatActivity() {

    private lateinit var foodImageView: ImageView
    private lateinit var foodNameTextView: TextView
    private lateinit var restoLocationTextView: TextView
    private lateinit var foodBrandTextView: TextView
    private lateinit var foodPriceTextView: TextView
    private lateinit var foodDescTextView: TextView
    private lateinit var foodRatingTextView: TextView
    private lateinit var foodProteinTextView: TextView
    private lateinit var foodCarbsTextView: TextView
    private lateinit var foodFatsTextView: TextView
    private lateinit var foodCaloriesTextView: TextView
    private lateinit var backButton: ImageButton
    private lateinit var etBuyFood: EditText
    private lateinit var removebtn: ImageButton
    private lateinit var addbtn: ImageButton
    private lateinit var buybtn: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        foodImageView = findViewById(R.id.imgFoodDetail)
        foodNameTextView = findViewById(R.id.txtFoodNameField)
        foodBrandTextView = findViewById(R.id.txtFoodBrandField)
        restoLocationTextView = findViewById(R.id.restoLocation)
        foodPriceTextView = findViewById(R.id.txtFoodPriceField)
        foodDescTextView = findViewById(R.id.txtFoodDescField)
        foodRatingTextView = findViewById(R.id.txtFoodRatingField)
        foodCarbsTextView = findViewById(R.id.txtCarbsField)
        foodProteinTextView = findViewById(R.id.txtProteinField)
        foodFatsTextView = findViewById(R.id.txtFatsField)
        foodCaloriesTextView = findViewById(R.id.txtFoodCaloriesField)
        buybtn = findViewById(R.id.buybtn)
        backButton = findViewById(R.id.back_button)
        etBuyFood = findViewById(R.id.etBuyFood)
        etBuyFood.setText(quantity.toString())
        removebtn = findViewById(R.id.removebtn)
        addbtn = findViewById(R.id.addbtn)
        var imageResource = 0
        var imageResto = 0

        // Handle remove button click (similar to CarsDetailsActivity)
        removebtn.setOnClickListener {
            if (quantity == 1) {
                Toast.makeText(this, "Quantity cannot be zero!", Toast.LENGTH_SHORT).show()
            } else {
                quantity--
                etBuyFood.setText(quantity.toString())
            }
        }

        // Handle add button click (similar to CarsDetailsActivity)
        addbtn.setOnClickListener {
            quantity++
            etBuyFood.setText(quantity.toString())
        }

        val intent = intent
        val currentUser = auth.currentUser

        var uid =" "
        var email =""

        if(currentUser != null)
        { uid = currentUser.uid
            email = currentUser.email.toString()
        }

        if (intent.hasExtra("foodData")) {
            val foodData = intent.getSerializableExtra("foodData") as HashMap<String, Any>

            // Extract food details from foodData (replace with your data structure)
            val foodName = foodData["name"] as String
            val foodBrand = foodData["resto"] as String
            val foodPrice = foodData["price"] as Long
            val foodDesc = foodData["desc"] as String
            val foodRating = foodData["rating"] as String
            val foodCalories = foodData["calories"] as String
            val foodProtein = foodData["Protein"] as String
            val foodCarbs = foodData["Carbs"] as String
            val foodFats = foodData["Fats"] as String


            // Set food details on UI elements
            foodNameTextView.text = foodName
            foodBrandTextView.text = foodBrand
            foodPriceTextView.text = foodPrice.toString()
            foodDescTextView.text = foodDesc
            foodRatingTextView.text = foodRating
            foodCaloriesTextView.text = foodCalories
            foodProteinTextView.text = foodProtein
            foodCarbsTextView.text = foodCarbs
            foodFatsTextView.text = foodFats



            // Handle image loading based on your data structure (replace placeholders)
            when {
                foodName.lowercase().contains("sushi") -> {
                    imageResource = R.drawable.sushi_protein
                    imageResto = R.drawable.sushihiro_logo
                }
                foodName.lowercase().contains("chicken sub") -> {
                    imageResource = R.drawable.chickensub_protein
                    imageResto = R.drawable.subway_logo
                }
                foodName.lowercase().contains("salad") -> {
                    imageResource = R.drawable.salad_healthy
                    imageResto = R.drawable.saladstop_logo
                }
                // Add more cases for different food types
            }
        restoLocationTextView.setOnClickListener {
            when {
                foodName.lowercase().contains("sushi") -> {
                    val intent = Intent(this, SushiHiroMapsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                foodName.lowercase().contains("chicken sub") -> {
                    val intent = Intent(this, SubwayMapsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                foodName.lowercase().contains("salad") -> {
                    val intent = Intent(this, SaladStopMapsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

            Glide.with(this)
                .load(imageResource)
                .into(foodImageView)
        } else {
            finish() // Handle case where no data is received
        }


        // Handle buy button click
        buybtn.setOnClickListener {
            quantity = etBuyFood.text.toString().toInt() // Get buy quantity

            // Validate quantity (optional)
            if (quantity <= 0) {
                Toast.makeText(this, "Invalid quantity. Please enter a positive value.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Extract car details from received intent or UI elements as needed
            val carName = foodNameTextView.text.toString()
            val foodPrice = quantity * foodPriceTextView.text.toString().toDouble()  // Assuming price is displayed as text
            val restoImg = imageResto
            var documentId = ""

            val transaction = Transaction(restoImg, quantity, foodPrice, carName, uid, email, documentId)
            val docRef = db.collection("transactions").add(transaction)
            docRef.addOnSuccessListener { documentReference ->
                transaction.documentId = documentReference.id
                Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener { e ->
                    // Handle transaction addition failure
                    e.printStackTrace()
                    Toast.makeText(this, "Transaction failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            db.collection("transactions").add(transaction)
                .addOnSuccessListener { documentReference -> documentId = documentReference.id
                    transaction.documentId = documentId
                    Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle transaction addition failure
                    e.printStackTrace()
                    Toast.makeText(this, "Transaction failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            Transactions.addTransactions(restoImg, quantity, foodPrice, carName, uid, email,transaction.documentId)
            val intentNavigate = Intent(this, MenuActivity::class.java)
            startActivity(intentNavigate)
            finish()


        }

        backButton.setOnClickListener {
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }

    }

}