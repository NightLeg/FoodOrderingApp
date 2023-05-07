package com.example.foodorderingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.foodorderingapp.databinding.ActivityOrderPlacementBinding
import com.example.foodorderingapp.databinding.ActivityOrderSuccessBinding

class OrderSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantModel:RestaurantModel?=intent.getParcelableExtra("RestaurantModel")
        val actionBar: ActionBar?=supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(false)

        binding.buttonDone.setOnClickListener{
            setResult(RESULT_OK)
            finish()
        }





    }
}