import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timeconversionapplication.Dday
import com.example.timeconversionapplication.Product
import com.example.timeconversionapplication.WorkTime
import com.example.timeconversionapplication.databinding.ListPlaceBinding
import com.example.timeconversionapplication.databinding.ListProductBinding

class MyAdapter(private var dataSet: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_PLACE = 1
    private val VIEW_TYPE_PRODUCT = 2

    inner class PlaceViewHolder(val binding: ListPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(time: WorkTime) {
            binding.placeName.text = time.place_name
            binding.wage.text = time.wage.toString()
            binding.date.text = time.date
        }
    }

    inner class ProductViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.memo.text = product.memo
            binding.productName.text = product.product_name
            binding.dday.text = product.Dday
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position]) {
            is WorkTime -> VIEW_TYPE_PLACE
            is Product -> VIEW_TYPE_PRODUCT
            else -> throw IllegalArgumentException("Invalid data at position $position")
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
                placeHolder.bind(dataSet[position] as WorkTime)
            }
            VIEW_TYPE_PRODUCT -> {
                val productHolder = holder as ProductViewHolder
                productHolder.bind(dataSet[position] as Product)
            }
            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: MutableList<Any>) {
        this.dataSet = newList
        notifyDataSetChanged()
    }

    fun addItem(item: Any){
        dataSet.add(item)
        notifyItemInserted(dataSet.size-1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: MutableList<Any>) {
        dataSet = newData
        notifyDataSetChanged()
    }
}
