import MyAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timeconversionapplication.JobRegisActivity
import com.example.timeconversionapplication.MyDatabase
import com.example.timeconversionapplication.Product
import com.example.timeconversionapplication.WorkTime
import com.example.timeconversionapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbHelper: MyDatabase.MyDBHelper
    private lateinit var adapter: MyAdapter
    private lateinit var adapter2: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = MyDatabase.MyDBHelper(requireContext())
        Log.d("TAG","home create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG","home create view")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerViews()
        binding.addWorkPlace.setOnClickListener {
            val intent = Intent(activity, JobRegisActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onResume() {
        Log.d("TAG","resume")
        super.onResume()
        updateRecyclerViews()
    }

    override fun onStop() {
        Log.d("TAG","stop")
        super.onStop()
        updateRecyclerViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbHelper.close()
    }

    fun refreshData(){
        updateRecyclerViews()
    }

    private fun setupRecyclerViews() {
        binding.recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView1.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        adapter = MyAdapter(mutableListOf())
        binding.recyclerView1.adapter = adapter

        adapter2 = MyAdapter(mutableListOf())
        binding.recyclerView2.adapter = adapter2
    }

    private fun updateRecyclerViews() {
        Log.d("TAG", "update recycler view")
        val productList = dbHelper.selectAll(MyDatabase.MyDBContract.Product.TABLE_NAME, Product::class.java)
        adapter.updateData(productList as MutableList<Any>)

        val placeList = dbHelper.selectAll(MyDatabase.MyDBContract.WorkTime.TABLE_NAME, WorkTime::class.java)
        adapter2.updateData(placeList as MutableList<Any>)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
