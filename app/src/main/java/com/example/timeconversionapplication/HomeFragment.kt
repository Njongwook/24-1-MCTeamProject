package com.example.timeconversionapplication

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.Selection.selectAll
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timeconversionapplication.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var dbHelper: MyDatabase.MyDBHelper
    private lateinit var adapter: MyAdapter

//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 수정: dbHelper 초기화
        dbHelper = MyDatabase.MyDBHelper(requireContext()) // 또는 requireActivity()를 사용할 수도 있음

//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }

        // 데이터베이스 작업
        val db = dbHelper.writableDatabase
        Log.d("TAG", "START")

        // Product 데이터 삽입
        val productArr = mutableListOf(
            Product("아이폰", 1500000, "도전! 사과농장주")
        )
        for (entry in productArr) {
            val myentry = MyDatabase.MyDBContract.Product
            val products = ContentValues().apply {
                put(myentry.product_name, entry.product_name)
                put(myentry.price, entry.price)
                put(myentry.memo, entry.memo)
            }
            Log.d("TAG", "Products:$products")

            try {
                val newRowId = db.insertOrThrow(myentry.TABLE_NAME, null, products)
                Log.d("TAG", newRowId.toString())
            } catch (e: SQLiteConstraintException) {
                db.update(myentry.TABLE_NAME, products, "${myentry.product_name} LIKE ?", arrayOf(entry.product_name))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.addWorkPlace.setOnClickListener {
            val intent = Intent(activity, JobRegisActivity::class.java)
            startActivity(intent)
        }

        // RecyclerView 설정
        binding.recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView1.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        // 데이터베이스에서 Product 데이터 가져오기
        val productList = dbHelper.selectAll(MyDatabase.MyDBContract.Product.TABLE_NAME, Product::class.java)

        // 어댑터 설정 및 RecyclerView에 어댑터 할당
        adapter = MyAdapter(productList as MutableList<Any>)
        binding.recyclerView1.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 수정: dbHelper 사용 후에 닫기
        dbHelper.close()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
