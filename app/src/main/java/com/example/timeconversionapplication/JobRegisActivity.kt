package com.example.timeconversionapplication

import android.app.Instrumentation.ActivityResult
import android.content.ContentValues
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.timeconversionapplication.databinding.PartTimeJobRegisBinding
import com.example.timeconversionapplication.databinding.PartTimeJobRegisDetailsBinding

class JobRegisActivity : AppCompatActivity() {
    private val dbHelper = MyDatabase.MyDBHelper(this)
    private lateinit var binding: PartTimeJobRegisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PartTimeJobRegisBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        // 신유빈 코드 시작 --------------------------------------------------------------------------------------------
//        // WorkPlace 타입의 데이터를 조회하여 MutableList<Any>로 변환 - db 연결에 필요
//        val getList: MutableList<Any> = dbHelper.selectAll("workplace", WorkPlace::class.java).toMutableList()
//
//        // 가져온 데이터를 로그로 출력합니다. - 없어도 됨. 디버깅 용
//        for (e in getList) {
//            Log.d("TAG", e.toString())
//        }
//
//        // 어댑터에 데이터를 설정합니다. - db 연결에 필요
//        val adapter = MyAdapter(getList)

        // 신유빈 코드 종료 --------------------------------------------------------------------------------------------
        binding = PartTimeJobRegisBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)

        var salaryStyle = 3
        binding.daySalary.setOnClickListener{
            salaryStyle = 1
            binding.daySalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue4))
            binding.weekSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.monthSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            Toast.makeText(applicationContext,"select daySalary",Toast.LENGTH_SHORT).show()
        }
        binding.weekSalary.setOnClickListener{
            salaryStyle = 2
            binding.daySalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.weekSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue4))
            binding.monthSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            Toast.makeText(applicationContext,"select weekSalary",Toast.LENGTH_SHORT).show()
        }
        binding.monthSalary.setOnClickListener{
            salaryStyle = 3
            binding.daySalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.weekSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue3))
            binding.monthSalary.setBackgroundColor(ContextCompat.getColor(this,R.color.blue4))
            Toast.makeText(applicationContext,"select monthSalary",Toast.LENGTH_SHORT).show()
        }


        binding.taxText.isEnabled = false
        binding.taxSpinner.isEnabled = false
        var taxStyle = 0
        binding.taxCheck.setOnCheckedChangeListener { _, isChecked ->
            binding.taxSpinner.isEnabled = isChecked
            if (!isChecked) {
                binding.taxText.isEnabled = false
            } else {
                taxStyle = 1
                binding.taxText.isEnabled = true
            }
        }

        binding.taxSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) { // '직접입력'이 선택된 경우
                    binding.tax.isEnabled = true
                } else {
                    binding.tax.isEnabled = false
                    taxStyle = 1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작 (필요한 경우 구현)
            }
        }

        binding.insuranceText.isEnabled = false
        binding.insuranceSpinner.isEnabled = false
        var insuranceStyle = 1
        binding.insuranceCheck.setOnCheckedChangeListener { _, isChecked ->
            binding.insuranceSpinner.isEnabled = isChecked
            if (!isChecked) {
                binding.insuranceText.isEnabled = false
            } else {
                insuranceStyle = 1
                binding.insuranceText.isEnabled = true
            }
        }

        binding.insuranceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1) { // '직접입력'이 선택된 경우
                    binding.insurance.isEnabled = true
                } else {
                    binding.insurance.isEnabled = false
                    insuranceStyle = 1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작 (필요한 경우 구현)
            }
        }

        val taxSpinner : Spinner = binding.taxSpinner
        val insuranceSpinner: Spinner = binding.insuranceSpinner // 스피너 참조 추가

        val taxAdpter = ArrayAdapter.createFromResource(this, R.array.tax_array, android.R.layout.simple_spinner_item)
        taxAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taxSpinner.adapter = taxAdpter

        val insuranceAdapter = ArrayAdapter.createFromResource(this, R.array.insurance_array, android.R.layout.simple_spinner_item)
        insuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        insuranceSpinner.adapter = insuranceAdapter


        binding.next.setOnClickListener {
            val input = binding.days.text.toString()
            val number = input.toIntOrNull()
            if (number != null) {
                if (number > 31 || number < 1) {
                    Toast.makeText(this, "1부터 31까지의 숫자만 입력하세요.", Toast.LENGTH_SHORT).show()
                } else {
                    intent.putExtra("tax",taxStyle)
                    intent.putExtra("insurance",insuranceStyle)
                    val intent = Intent(this, JobRegisDetailActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "유효한 숫자를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 신유빈 코드 시작 --------------------------------------------------------------------------------------------
////        // 버튼 누르는 것에 따라 일급, 주급, 월급 구분
////        var salaryStyle = 0
////        binding.daySalary.setOnClickListener{
////            salaryStyle = 1
////            Log.d("TAG", salaryStyle.toString())
////        }
////        binding.weekSalary.setOnClickListener{
////            salaryStyle = 2
////            Log.d("TAG", salaryStyle.toString())
////        }
////        binding.monthSalary.setOnClickListener{
////            salaryStyle = 3
////            Log.d("TAG", salaryStyle.toString())
////        }
//        //다음 버튼 누르면 해당 화면에 있는 근무지 정보 db에 저장하기
//        binding.next.setOnClickListener {
//            // input 창에서 근무지명 받아오기
//            val placeName = binding.partTimeJobName.text.toString()
//            // input 창에서 월급일 받아오기
//            val salaryDayText = binding.days.text.toString()
//
//            // 안 넣은 값 있는지 검사
//            // 위에서 주석 처리 한 부분에서 salaryStyle 디폴트 값을 0으로 설정 했어서 검사도 0인지 알아봄
//            if (placeName.isBlank() || salaryStyle == 0){
//                Log.d("TAG", "All fields must be filled out")
//                Log.d("TAG", placeName)
//                Log.d("TAG", salaryStyle.toString())
//                return@setOnClickListener
//            }
//            // 월급일이 숫자인지 검사
//            val salaryDay: Int
//            try {
//                salaryDay = salaryDayText.toInt()
//
//            } catch (e: NumberFormatException) {
//                Log.d("TAG", "Salary day must be a number  Provided: '$salaryDayText'")
//                return@setOnClickListener
//            }
//
//            // 화면에서 받은 값 들로 새 WorkPlace 값 생성 - db 연결에 필요
//            // tax는 일단 0으로 설정
//            val newElement = WorkPlace(placeName, salaryStyle, salaryDay, 0)
//
//            // 데이터베이스에 새로운 WorkPlace 객체 삽입 - db 연결에 필요
//            dbHelper.use {
//                val db = dbHelper.writableDatabase
//                val values = ContentValues().apply {
//                    put("place_name", placeName)
//                    put("salary_style", salaryStyle)
//                    put("salary_day", salaryDay)
//                    put("tax", 0)
//                }
//                // db에 삽입
//                db.insert("workplace", null, values)
//            }
//
//            // 아답터에 값 넣기 - db 연결에 필요
//            adapter.addItem(newElement)
//
//            // 디버그 용 - 없어도 됨
//            Log.d("TAG", newElement.toString())
//
//            // 다음 화면(JobRegisDetail)으로 넘어가기
//            val intent = Intent(this, JobRegisDetailActivity::class.java)
//            startActivity(intent)
//
//            //super.onCreate(savedInstanceState)
//        }
        // 신유빈 코드 종료 --------------------------------------------------------------------------------------------
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}