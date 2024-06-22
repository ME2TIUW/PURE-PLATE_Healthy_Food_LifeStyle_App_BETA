package com.example.pureplate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pureplate.Adapter.FoodsListAdapter

class FoodsActivity : AppCompatActivity() {

    lateinit var selectedCategory: String
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foods)

        recyclerView = findViewById(R.id.rvFoods) // Find RecyclerView by ID

        // Get the selected category from the Intent extra
        selectedCategory = intent.getStringExtra("selectedCategory") ?: ""

        // Set up RecyclerView with adapter
        val adapter = FoodsListAdapter(this, ::onFoodClicked, selectedCategory) // Pass context and click listener
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun onFoodClicked(foodData: HashMap<String, Any>) {
        // Handle food item click (optional)
    }
}
