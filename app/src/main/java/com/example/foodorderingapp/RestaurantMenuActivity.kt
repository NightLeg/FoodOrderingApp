package com.example.foodorderingapp

import android.app.Notification.Action
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodorderingapp.adapter.MenuListAdapter
import com.example.foodorderingapp.databinding.ActivityRestaurantMenuBinding

class RestaurantMenuActivity : AppCompatActivity(), MenuListAdapter.MenuListClickListener{
    private var menuList: List<Menus?>?=null
    private var totalItemINCart=0
    private var itemListInCart:MutableList<Menus?>?=null
    private var menuListAdapter:MenuListAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityRestaurantMenuBinding.inflate(layoutInflater)

        setContentView(binding.root)



        val restaurantModel=intent?.getParcelableExtra<RestaurantModel>("RestaurantModel")
        val actionBar: ActionBar?=supportActionBar
        actionBar?.setTitle(restaurantModel?.name)
        actionBar?.setSubtitle(restaurantModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        menuList=restaurantModel?.menus

        binding.menuRecyclerView.layoutManager=GridLayoutManager(this,2)
        menuListAdapter=MenuListAdapter(menuList,this)
        binding.menuRecyclerView.adapter=menuListAdapter

        binding.checkOutButton.setOnClickListener{
            if(itemListInCart!=null&&itemListInCart!!.size<=0)
            {
                Toast.makeText(this@RestaurantMenuActivity, "Please add some item into the Cart", Toast.LENGTH_SHORT).show()
            }
            else
            {
                restaurantModel?.menus=itemListInCart
                val intent= Intent(this@RestaurantMenuActivity,OrderPlacement::class.java)
                intent.putExtra("RestaurantModel",restaurantModel)
                startActivityForResult(intent, 1000)
            }

        }




    }

    override fun addToCartClickListener(menu: Menus,binding:ActivityRestaurantMenuBinding) {
        if(itemListInCart==null)
        {
            itemListInCart=ArrayList()

        }
        itemListInCart?.add(menu)
        totalItemINCart=0
        for(menu in itemListInCart!!)
        {
            totalItemINCart += menu?.totalInCart!!
        }
        binding.checkOutButton.text="Checkout ("+ totalItemINCart+") Items"


    }

    override fun updateCartClickListener(menu: Menus,binding:ActivityRestaurantMenuBinding) {
        val index=itemListInCart!!.indexOf(menu)
        itemListInCart?.removeAt(index)
        itemListInCart?.add(menu)
        totalItemINCart=0
        for(menu in itemListInCart!!)
        {
            totalItemINCart+=menu?.totalInCart!!
        }
        binding.checkOutButton.text="Check Out ("+totalItemINCart+") Items"
    }

    override fun removeFromCartClickListener(menu: Menus,binding:ActivityRestaurantMenuBinding) {
        if(itemListInCart!!.contains(menu))
        {
            itemListInCart?.remove(menu)
            totalItemINCart=0
            for(menu in itemListInCart!!)
            {
                totalItemINCart+=menu?.totalInCart!!
            }
            binding.checkOutButton.text="Check Out ("+totalItemINCart+") Items"
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home->finish()
            else->{}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000 && resultCode == RESULT_OK) {
            finish()
        }
    }


}