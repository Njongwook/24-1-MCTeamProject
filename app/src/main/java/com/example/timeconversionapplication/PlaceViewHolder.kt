package com.example.timeconversionapplication

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.timeconversionapplication.databinding.ListPlaceBinding

public class PlaceViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    val deleteButton: AppCompatButton = itemView.findViewById(R.id.delete)


}