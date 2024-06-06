package com.example.timeconversionapplication

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timeconversionapplication.databinding.FragmentCalendarBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding
    private lateinit var dbHelper: MyDatabase.MyDBHelper
    private lateinit var adapter: CalendarAdapter
    private lateinit var adapter2: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = MyDatabase.MyDBHelper(requireContext())

        // 데이터베이스 작업
        val db = dbHelper.writableDatabase
        Log.d("TAG", "START")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // 날짜가 변경될 때 day 값 업데이트 및 DB에서 데이터 조회
        var day = ""
        binding.calenderView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            binding.month.text = (month+1).toString()
            binding.day.text = dayOfMonth.toString()

            // 해당 날짜에 일한 정보 있으면 db에서 찾아와서 일급 넣기
            day = "${year}년 ${month+1}월 ${dayOfMonth}일"
            val workTime = dbHelper.selectByDate(day)
            if (workTime != null) {
                binding.wage.text = workTime.wage.toString()
            }
        }

        // RecyclerView 설정 - recyclerView1은  일급, recyclerView2 는 디데이
        binding.recyclerView1.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView1.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        // 데이터베이스에서 WorkTime 데이터 가져오기
        val salaryList = dbHelper.selectAll(MyDatabase.MyDBContract.WorkTime.TABLE_NAME, WorkTime::class.java)
        // 데이터베이스에서 Dday 데이터 가져오기
        val ddayList = dbHelper.selectAll(MyDatabase.MyDBContract.Dday.TABLE_NAME, Dday::class.java)

        // 어댑터 설정 및 RecyclerView에 어댑터 할당
        adapter = CalendarAdapter(salaryList as MutableList<Any>)
        binding.recyclerView1.adapter = adapter
        adapter2 = CalendarAdapter(ddayList as MutableList<Any>)
        binding.recyclerView2.adapter = adapter2

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}
