package com.shopfully.koutatshopfully

import android.content.DialogInterface
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shopfully.koutatshopfully.databinding.ItemProductBinding

class ProductViewHolder(private val view: ItemProductBinding) : RecyclerView.ViewHolder(view.root) {
    fun render(ProductModel: Product, onClickListener: (Product) -> Unit) {
        view.apply {
            title.text = ProductModel.title
            Glide.with(imageView.context)
                .load("https://it-it-media.shopfully.cloud/images/volantini/${ProductModel.id}@3x.jpg")
                .into(imageView)
            textView3.text = ProductModel.read.toString()
            itemView.setOnClickListener{
                onClickListener(ProductModel)
            }
        }
    }
}