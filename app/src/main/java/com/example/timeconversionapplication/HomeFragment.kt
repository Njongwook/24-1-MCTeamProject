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
    private lateinit var adapter2: MyAdapter
    private lateinit var adapter3: PlaceAdapter

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

        // SAMPLE : Product 데이터 삽입
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

        // SAMPLE : Dday 데이터 삽입
        val ddayArr = mutableListOf(
            Dday(100, 50, "아이폰", 1500000, "도전! 사과농장주", "버거킹", 10000)
        )
        for (entry in ddayArr) {
            val myentry = MyDatabase.MyDBContract.Dday
            val ddays = ContentValues().apply {
                put(myentry.Dtime, entry.Dtime)
                put(myentry.Dday, entry.Dday)
                put(myentry.product_name, entry.product_name)
                put(myentry.product_price, entry.product_price)
                put(myentry.product_memo, entry.product_memo)
                put(myentry.place_name, entry.place_name)
                put(myentry.hourly, entry.hourly)
            }
            Log.d("TAG", "Ddays:$ddays")

            try {
                val newRowId = db.insertOrThrow(myentry.TABLE_NAME, null, ddays)
                Log.d("TAG", newRowId.toString())
            } catch (e: SQLiteConstraintException) {
                db.update(myentry.TABLE_NAME, ddays, "${myentry.product_name} LIKE ?", arrayOf(entry.product_name))
            }
        }

//        // SAMPLE : WorkPlace 데이터 삽입
//        val placeArr = mutableListOf(
//            WorkPlace("버거킹", 3, 15, 10000, 1)
//        )
//        for (entry in placeArr) {
//            val myentry = MyDatabase.MyDBContract.WorkPlace
//            val products = ContentValues().apply {
//                put(myentry.place_name, entry.place_name)
//                put(myentry.salary_style, entry.salary_style)
//                put(myentry.salary_day, entry.salary_day)
//                put(myentry.hourly, entry.hourly)
//                put(myentry.tax, entry.tax)
//            }
//            Log.d("TAG", "Products:$products")
//
//            try {
//                val newRowId = db.insertOrThrow(myentry.TABLE_NAME, null, products)
//                Log.d("TAG", newRowId.toString())
//            } catch (e: SQLiteConstraintException) {
//                db.update(myentry.TABLE_NAME, products, "${myentry.place_name} LIKE ?", arrayOf(entry.place_name))
//            }
//        }
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

        // RecyclerView 설정 - recyclerView1은 상품 정보, recyclerView2 는 근무 정보
        binding.recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView1.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        // 데이터베이스에서 Dday 데이터 가져오기
        val productList = dbHelper.selectAll(MyDatabase.MyDBContract.Dday.TABLE_NAME, Dday::class.java)

        // 데이터베이스에서 WorkPlace 데이터 가져오기
        val placeList = dbHelper.selectAll(MyDatabase.MyDBContract.WorkPlace.TABLE_NAME, WorkPlace::class.java)

        // 어댑터 설정 및 RecyclerView에 어댑터 할당
        adapter = MyAdapter(productList as MutableList<Any>)
        binding.recyclerView1.adapter = adapter
        adapter2 = MyAdapter(placeList as MutableList<Any>)
        binding.recyclerView2.adapter = adapter2

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
