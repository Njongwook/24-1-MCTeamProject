package com.example.timeconversionapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(private val placeList: List<WorkPlace>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(place: WorkPlace)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.place_name)

        fun bind(place: WorkPlace, clickListener: OnItemClickListener) {
            placeName.text = place.place_name
            itemView.setOnClickListener {
                clickListener.onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_place, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(placeList[position], itemClickListener)
    }

    override fun getItemCount() = placeList.size
}
