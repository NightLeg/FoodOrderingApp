package com.example.foodorderingapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.OrderPlacementAdapter
import com.example.foodorderingapp.adapter.UserData
import com.example.foodorderingapp.databinding.ActivityOrderPlacementBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson


class OrderPlacement : AppCompatActivity() {

    var orderPlacement: OrderPlacementAdapter?=null
    var isDeliveryOn:Boolean=false
    var addrress:String="null"
    var restaurantName:String?="null"
    var food:List<Menus?>?=null


    private lateinit var database:DatabaseReference
    lateinit var userData:UserData




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityOrderPlacementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantModel:RestaurantModel?=intent.getParcelableExtra("RestaurantModel")
        val actionBar:ActionBar?=supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonPlaceYourOrder.setOnClickListener {
            onPlaceOrderButtonClick(restaurantModel,binding)
            addrress=String.format("%s,%s,%s,%s",binding.inputAddress.text.toString(),binding.inputCity.text.toString(),binding.inputState.text.toString(),binding.inputZip.text.toString())

            restaurantName=restaurantModel?.name
            food=restaurantModel?.menus





            database=FirebaseDatabase.getInstance().getReference(restaurantName!!)
            var id=database.push().key.toString()
            userData= UserData(id,restaurantName,addrress,food)
            database.child(userData.id!!).setValue(userData)









        }


        binding.switchDelivery?.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked) {
                binding.inputAddress.visibility = View.VISIBLE
                binding.inputCity.visibility = View.VISIBLE
                binding.inputState.visibility = View.VISIBLE
                binding.inputZip.visibility = View.VISIBLE
                binding.tvDeliveryCharge.visibility = View.VISIBLE
                binding.tvDeliveryChargeAmount.visibility = View.VISIBLE
                isDeliveryOn=true

            } else {
                binding.inputAddress.visibility = View.GONE
                binding.inputCity.visibility = View.GONE
                binding.inputState.visibility = View.GONE
                binding.inputZip.visibility = View.GONE
                binding.tvDeliveryCharge.visibility = View.GONE
                binding.tvDeliveryChargeAmount.visibility = View.GONE
                isDeliveryOn=false

            }
        }


        binding.cartItemsRecyclerView.layoutManager=LinearLayoutManager(this)
        orderPlacement= OrderPlacementAdapter(restaurantModel?.menus)
        binding.cartItemsRecyclerView.adapter=orderPlacement

        calculateTotal(restaurantModel,binding)

    }




    private fun calculateTotal(restaurantModel:RestaurantModel?,binding: ActivityOrderPlacementBinding)
    {
        var subTotal=0f
        for(menu in restaurantModel?.menus!!)
        {
            subTotal+=menu?.price!!.toFloat() * menu?.totalInCart!!
        }

        binding.tvSubtotalAmount.text="RM"+String.format("%.2f",subTotal)
        if(isDeliveryOn)
        {
            binding.tvDeliveryChargeAmount.text="RM"+String.format("%.2f",restaurantModel.delivery_charge?.toFloat())
            subTotal+=restaurantModel?.delivery_charge?.toFloat()!!
        }

        binding.tvTotalAmount.text="RM"+String.format("%.2f",subTotal)


    }

    private fun onPlaceOrderButtonClick(restaurantModel: RestaurantModel?,binding: ActivityOrderPlacementBinding)
    {
        if(TextUtils.isEmpty(binding.inputName.text.toString())) {
            binding.inputName.error =  "Enter your name"
            return
        } else if(isDeliveryOn && TextUtils.isEmpty(binding.inputAddress.text.toString())) {
            binding.inputAddress.error =  "Enter your address"
            return
        } else if(isDeliveryOn && TextUtils.isEmpty(binding.inputCity.text.toString())) {
            binding.inputCity.error =  "Enter your City Name"
            return
        } else if(isDeliveryOn && TextUtils.isEmpty(binding.inputZip.text.toString())) {
            binding.inputZip.error =  "Enter your Zip code"
            return
        } else if( TextUtils.isEmpty(binding.inputCardNumber.text.toString())) {
            binding.inputCardNumber.error =  "Enter your credit card number"
            return
        } else if( TextUtils.isEmpty(binding.inputCardExpiry.text.toString())) {
            binding.inputCardExpiry.error =  "Enter your credit card expiry"
            return
        } else if( TextUtils.isEmpty(binding.inputCardPin.text.toString())) {
            binding.inputCardPin.error =  "Enter your credit card pin/cvv"
            return
        }
        val intent = Intent(this@OrderPlacement, OrderSuccessActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivityForResult(intent, 1000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1000)
        {
            setResult(RESULT_OK)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home->finish()
            else->{}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }


}