package com.example.foodorderingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.LayoutInflaterCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.Menus
import com.example.foodorderingapp.R
import com.example.foodorderingapp.RestaurantMenuActivity
import com.example.foodorderingapp.databinding.ActivityRestaurantMenuBinding

class MenuListAdapter(val menuList:List<Menus?>?, val clickListener: MenuListAdapter.MenuListClickListener) :RecyclerView.Adapter<MenuListAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuListAdapter.MyViewHolder {
        val binding = ActivityRestaurantMenuBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.menu_list,parent,false)
        return MyViewHolder(view,binding)
    }

    override fun onBindViewHolder(holder: MenuListAdapter.MyViewHolder, position: Int) {

        holder.bind(menuList?.get(position)!!)

    }

    override fun getItemCount(): Int {
        if(menuList==null)return 0
        else return menuList.size
    }

    inner class MyViewHolder(view: View,binding: ActivityRestaurantMenuBinding):RecyclerView.ViewHolder(view) {
        var thumbImage:ImageView=view.findViewById(R.id.thumbImage)
        val menuName:TextView=view.findViewById(R.id.menuName)
        val menuPrice:TextView=view.findViewById(R.id.menuPrice)
        val addToCartButton:TextView=view.findViewById(R.id.addToCartButton)
        val addMoreLayout:LinearLayout=view.findViewById(R.id.addMoreLayout)
        val imageMinus:ImageView=view.findViewById(R.id.imageMinus)
        val imageAddOne:ImageView=view.findViewById(R.id.imageAddOne)
        val tvCount:TextView=view.findViewById(R.id.tvCount)
        val binding = binding

        fun bind(menus: Menus)
        {
            menuName.text=menus?.name
            menuPrice.text="Price : RM ${menus?.price}"
            addToCartButton.setOnClickListener {
                menus?.totalInCart=1
                clickListener.addToCartClickListener(menus, binding)
                addMoreLayout?.visibility=View.VISIBLE
                addToCartButton.visibility=View.GONE
                tvCount.text=menus?.totalInCart.toString()

            }
            imageMinus.setOnClickListener {
                var total:Int= menus.totalInCart
                total--
                if(total>0)
                {
                    menus?.totalInCart=total
                    clickListener.updateCartClickListener(menus,binding)
                    tvCount.text=menus?.totalInCart.toString()
                }
                else{
                    menus.totalInCart=total
                    addMoreLayout.visibility=View.GONE
                    addToCartButton.visibility=View.VISIBLE
                }

            }
            imageAddOne.setOnClickListener {
                var total:Int=menus.totalInCart
                total++
                if(total<=10)
                {
                    menus.totalInCart=total
                    clickListener.updateCartClickListener(menus,binding)
                    tvCount.text=total.toString()
                }
            }

            Glide.with(thumbImage).load(menus?.url).into(thumbImage)

        }
    }

    interface MenuListClickListener{
        fun addToCartClickListener(menu: Menus,binding: ActivityRestaurantMenuBinding)
        fun updateCartClickListener(menu: Menus,binding: ActivityRestaurantMenuBinding)
        fun removeFromCartClickListener(menu: Menus,binding: ActivityRestaurantMenuBinding)
    }

}