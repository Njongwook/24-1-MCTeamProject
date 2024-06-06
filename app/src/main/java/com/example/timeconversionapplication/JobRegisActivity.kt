package com.example.timeconversionapplication

import MyAdapter
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
        val getList: MutableList<Any> = dbHelper.selectAll("workplace", WorkPlace::class.java).toMutableList()
        for (e in getList) {
            Log.d("TAG", e.toString())
        }
        val adapter = MyAdapter(getList)
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

        var salaryStyle = 0
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

        var taxStyle = 0.0

        binding.taxCheck.setOnCheckedChangeListener { _, isChecked ->
            binding.taxSpinner.isEnabled = isChecked
            if (!isChecked) {
                binding.taxText.isEnabled = false
                binding.tax.isEnabled = false
                taxStyle = 0.0 // 체크되지 않은 경우 taxStyle을 0.0으로 고정
            } else {
                binding.taxText.isEnabled = true
                taxStyle = 1.0
            }
        }
        binding.taxSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (binding.taxCheck.isChecked) {
                    if (position == 0) {
                        binding.tax.isEnabled = false
                        taxStyle = 1.0
                    } else {
                        binding.tax.isEnabled = true
                        taxStyle = 2.0
                    }
                } else {
                    taxStyle = 0.0 // taxCheck가 체크되지 않은 경우 taxStyle을 0.0으로 고정
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작 (필요한 경우 구현)
            }
        }

        val taxSpinner : Spinner = binding.taxSpinner

        val taxAdpter = ArrayAdapter.createFromResource(this, R.array.tax_array, android.R.layout.simple_spinner_item)
        taxAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taxSpinner.adapter = taxAdpter

        val insuranceAdapter = ArrayAdapter.createFromResource(this, R.array.insurance_array, android.R.layout.simple_spinner_item)
        insuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.next.setOnClickListener {
            val day = binding.days.text.toString()
            val placeName = binding.partTimeJobName.text.toString()
            val salaryDayText = day.toIntOrNull()
            // 안 넣은 값 있는지 검사
            // 위에서 주석 처리 한 부분에서 salaryStyle 디폴트 값을 0으로 설정 했어서 검사도 0인지 알아봄
            if (salaryDayText != null && placeName.isNotEmpty() && salaryStyle != 0){
                val salaryDay = salaryDayText ?: 0 // null이면 기본값 0으로 설정

                // 화면에서 받은 값 들로 새 WorkPlace 값 생성 - db 연결에 필요
                // tax는 일단 0으로 설정
                val newElement = WorkPlace(placeName, salaryStyle, salaryDay, 0)

                // 데이터베이스에 새로운 WorkPlace 객체 삽입 - db 연결에 필요
                dbHelper.use {
                    val db = dbHelper.writableDatabase
                    val values = ContentValues().apply {
                        put("place_name", placeName)
                        put("salary_style", salaryStyle)
                        put("salary_day", salaryDay)
                        put("tax", when (taxStyle) {
                            2.0 -> binding.tax.text.toString().toDoubleOrNull() ?: 0.0
                            1.0 -> 3.3
                            else -> 0.0
                        })
                    }
                    // db에 삽입
                    db.insert("workplace", null, values)
                }


                // 아답터에 값 넣기 - db 연결에 필요
                adapter.addItem(newElement)

                // 디버그 용 - 없어도 됨
                Log.d("TAG", newElement.toString())



                // 다음 화면(JobRegisDetail)으로 넘어가기
                val intent = Intent(this, JobRegisDetailActivity::class.java)
                intent.putExtra("place_name",placeName)
                intent.putExtra("tax", when (taxStyle) {
                    2.0 -> binding.tax.text.toString().toDoubleOrNull() ?: 0.0
                    1.0 -> 3.3
                    else -> 0.0
                })
                startActivity(intent)

                //super.onCreate(savedInstanceState)
            } else {
                Log.d("TAG", "All fields must be filled out")
                Log.d("TAG", placeName)
                Log.d("TAG", salaryStyle.toString())
                Log.d("TAG", day)
                Log.d("TAG", taxStyle.toString())
                Toast.makeText(this, "모든 값을 전부 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}