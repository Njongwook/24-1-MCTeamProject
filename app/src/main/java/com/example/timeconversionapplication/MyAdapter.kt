import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timeconversionapplication.MyDatabase
import com.example.timeconversionapplication.Product
import com.example.timeconversionapplication.WorkTime
import com.example.timeconversionapplication.databinding.ListPlaceBinding
import com.example.timeconversionapplication.databinding.ListProductBinding

class MyAdapter(private var dataSet: MutableList<Any>, private val dbHelper: MyDatabase.MyDBHelper) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_PLACE = 1
    private val VIEW_TYPE_PRODUCT = 2

    inner class PlaceViewHolder(val binding: ListPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(time: WorkTime) {
            binding.placeName.text = time.place_name
            binding.wage.text = time.wage.toString()
            binding.date.text = time.date

            binding.delete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    dbHelper.deleteWorkTime(time.date)  // 데이터베이스에서 삭제
                    dataSet.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, dataSet.size)
                }
            }
        }
    }

    inner class ProductViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.memo.text = product.memo
            binding.productName.text = product.product_name
            binding.dday.text = product.Dday

            binding.delete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    dbHelper.deleteProduct(product.product_name)  // 데이터베이스에서 삭제
                    dataSet.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, dataSet.size)
                }
            }
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
    fun updateData(newData: MutableList<Any>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    fun addItem(item: Any) {
        dataSet.add(item)
        notifyItemInserted(dataSet.size - 1)
    }
}
