package com.example.foodorderingapp.adapter

import com.example.foodorderingapp.Menus
import com.example.foodorderingapp.RestaurantModel
import com.google.firebase.database.Exclude

data class UserData(
    @get:Exclude
    var id :String,
    var restaurantName: String?,
    var address: String,
    var food:List<Menus?>?
)