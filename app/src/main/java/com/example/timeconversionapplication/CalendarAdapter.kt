package com.example.timeconversionapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timeconversionapplication.databinding.ListCalddayBinding
import com.example.timeconversionapplication.databinding.ListDaysalaryBinding
import com.example.timeconversionapplication.databinding.ListProductBinding

class CalendarAdapter(private var dataSet: MutableList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_SALARY = 1
    private val VIEW_TYPE_DDAY = 2
    private lateinit var itemClickListener : OnItemClickListener


    inner class SalaryViewHolder(val binding: ListDaysalaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(time: WorkTime) {
            binding.placeName.text = time.place_name
            binding.wage.text = time.wage.toString()
        }
    }

    inner class DdayViewHolder(val binding: ListCalddayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dday: Dday) {
            binding.productName.text = dday.product_name
            binding.dday.text = dday.Dday.toString()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = dataSet[position]) {
            is WorkTime -> VIEW_TYPE_SALARY
            is Dday -> VIEW_TYPE_DDAY
            else -> {
                Log.e("MyAdapter", "Invalid data at position $position: ${item::class.java.simpleName}")
                throw IllegalArgumentException("Invalid data at position $position")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SALARY -> {
                val binding = ListDaysalaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SalaryViewHolder(binding)
            }
            VIEW_TYPE_DDAY -> {
                val binding = ListCalddayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DdayViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_SALARY -> {
                val salaryHolder = holder as SalaryViewHolder
                salaryHolder.bind(dataSet[position] as WorkTime)
            }
            VIEW_TYPE_DDAY -> {
                val ddayHolder = holder as DdayViewHolder
                val dday = dataSet[position] as? Dday ?: return
                ddayHolder.bind(dday)
            }
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    fun setList(newList: MutableList<Any>) {
        this.dataSet = newList
        notifyDataSetChanged()
    }

    fun getElement(pos:Int): Any {
        return dataSet[pos]
    }

    fun addItem(item: Any){
        dataSet.add(item)
        this.notifyItemInserted(dataSet.size-1)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }
}