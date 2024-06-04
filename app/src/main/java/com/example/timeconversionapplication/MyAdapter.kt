package com.example.timeconversionapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timeconversionapplication.databinding.ListPlaceBinding
import com.example.timeconversionapplication.databinding.ListProductBinding

class MyAdapter(private var dataSet: MutableList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_PLACE = 1
    private val VIEW_TYPE_PRODUCT = 2

    inner class PlaceViewHolder(val binding: ListPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: WorkPlace) {
            binding.placeName.text = place.place_name
        }
    }

    inner class ProductViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dday: Dday) {
            binding.memo.text = dday.product_memo
            binding.productName.text = dday.product_name
            binding.dday.text = dday.Dday.toString()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = dataSet[position]) {
            is WorkPlace -> VIEW_TYPE_PLACE
            is Dday -> VIEW_TYPE_PRODUCT
            else -> {
                Log.e("MyAdapter", "Invalid data at position $position: ${item::class.java.simpleName}")
                throw IllegalArgumentException("Invalid data at position $position")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PLACE -> {
                val binding = ListPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PlaceViewHolder(binding)
            }
            VIEW_TYPE_PRODUCT -> {
                val binding = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProductViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_PLACE -> {
                val placeHolder = holder as PlaceViewHolder
                placeHolder.bind(dataSet[position] as WorkPlace)
            }
            VIEW_TYPE_PRODUCT -> {
                val productHolder = holder as ProductViewHolder
                val dday = dataSet[position] as? Dday ?: return
                productHolder.bind(dday)
            }
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    fun setList(newList: MutableList<Any>) {
        this.dataSet = newList
        notifyDataSetChanged()
    }
}
