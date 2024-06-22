package com.example.pureplate.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.pureplate.FoodsActivity
import com.example.pureplate.R


        private lateinit var selectedCategory: String
        private lateinit var ivVegan: ImageView
        private lateinit var ivProtein: ImageView
        private lateinit var ivHealthy: ImageView
        private lateinit var ivCarbs: ImageView
        private lateinit var ivSubway: ImageView
        private lateinit var ivSalad: ImageView
        private lateinit var ivSushi: ImageView




    class HomeFragment : Fragment() {




        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_home, container, false)
            ivVegan = view.findViewById<ImageView>(R.id.ivVegan)
            ivProtein = view.findViewById<ImageView>(R.id.ivProtein)
            ivHealthy = view.findViewById<ImageView>(R.id.ivHealthy)
            ivCarbs = view.findViewById<ImageView>(R.id.ivCarbs)
            ivSubway = view.findViewById<ImageView>(R.id.subway_tile)
            ivSalad = view.findViewById<ImageView>(R.id.saladstop_tile)
            ivSushi = view.findViewById<ImageView>(R.id.sushiHiro_tile)



            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Set click listeners for category ImageViews
            ivVegan.setOnClickListener {
                selectedCategory = "Healthy"
                navigateToFoodsActivity()
            }
            ivHealthy.setOnClickListener {
                selectedCategory = "Healthy"
                navigateToFoodsActivity()
            }
            ivProtein.setOnClickListener {
                selectedCategory = "Protein"
                navigateToFoodsActivity()
            }
            ivCarbs.setOnClickListener {
                selectedCategory = "Carbs"
                navigateToFoodsActivity()
            }
            ivSalad.setOnClickListener {
                selectedCategory = "Healthy"
                navigateToFoodsActivity()
            }
            ivSubway.setOnClickListener {
                selectedCategory = "Protein"
                navigateToFoodsActivity()
            }
            ivSushi.setOnClickListener {
                selectedCategory = "Carbs"
                navigateToFoodsActivity()
            }

        }

        private fun navigateToFoodsActivity() {
            val intent = Intent(requireContext(), FoodsActivity::class.java)
            intent.putExtra("selectedCategory", selectedCategory)
            startActivity(intent)
        }
    }

