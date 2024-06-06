package com.example.timeconversionapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import com.example.timeconversionapplication.databinding.FragmentProductBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.ceil

class ProductFragment : Fragment(), ProductDialogFragment.OnProductSelectedListener {
    private lateinit var dbHelper: MyDatabase.MyDBHelper
    private lateinit var binding: FragmentProductBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        dbHelper = MyDatabase.MyDBHelper(requireContext())
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        binding.findPrice.apply {
            setOnClickListener {
                val productName = binding.productName.text.toString()
                if (productName.isNotBlank()) {
                    val clientId = "FVG3wWlAd0pWsd2o4gf2"
                    val clientSecret = "6wh5CKV19L"
                    val apiURL = "https://openapi.naver.com/v1/search/shop?query=${productName}"

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = get(apiURL, clientId, clientSecret)
                        withContext(Dispatchers.Main) {
                            if (response.isNotEmpty()) {
                                val jsonResponse = JSONObject(response)
                                val items = jsonResponse.getJSONArray("items")
                                val itemLength = items.length()

                                if (itemLength > 0) {
                                    val dialogFragment = ProductDialogFragment.newInstance(response)
                                    dialogFragment.setTargetFragment(this@ProductFragment, 0)
                                    dialogFragment.show(parentFragmentManager, "ProductDialogFragment")

                                    Log.d("API", "total item q$itemLength")
                                    for (i in 0 until itemLength) {
                                        Log.d("API", "object $i: ${items.getJSONObject(i)}")
                                    }
                                } else {
                                    Toast.makeText(context, "검색 결과가 없습니다!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "검색 결과가 없습니다!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "상품명을 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.productSave.apply {
            setOnClickListener {
                val name = binding.productName.text.toString()
                val priceText = binding.productPrice.text.toString()
                var memo = binding.memo.text.toString()

                if (name.isBlank() || priceText.isBlank()) {
                    Log.d("TAG", "All fields must be filled out")
                    Toast.makeText(context, "All fields must be filled out", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (memo.isBlank()) {
                    memo = ""
                }

                val price = priceText.toInt()

                // Dtime, Dday 계산 기능 구현
                var Dtime = 0
                var Dday = 0
                var salary = 0
                var hourlySum = 0

                // workTime의 wage 값 모두 더해서 salary 만들기
                dbHelper.use {
                    val db = dbHelper.readableDatabase

                    val cursor = db.rawQuery("SELECT wage FROM worktime", null)
                    if (cursor.moveToFirst()) {
                        do {
                            val wage = cursor.getInt(cursor.getColumnIndexOrThrow("wage"))
                            salary += wage
                        } while (cursor.moveToNext())
                    }
                    cursor.close()

                    val cursor2 = db.rawQuery("SELECT hourly FROM worktime", null)
                    if (cursor2.moveToFirst()) {
                        do {
                            val hourly = cursor2.getInt(cursor2.getColumnIndexOrThrow("hourly"))
                            hourlySum += hourly
                        } while (cursor2.moveToNext())
                    }
                    cursor2.close()

                    Dtime = ceil(price.toDouble() / hourlySum).toInt()
                    Dday = ceil(price.toDouble() / salary).toInt()

                    // dday 데이터 저장
                    val ddayValues = ContentValues().apply {
                        put("Dtime", Dtime)
                        put("Dday", Dday)
                        put("product_name", name)
                        put("product_price", price)
                        put("salary", salary)
                    }

                    db.insert("dday", null, ddayValues)
                }

                Log.d("TAG", "Dtime: $Dtime")
                Log.d("TAG", "Dday: $Dday")

                val newElement = Product(name, price, memo, Dday.toString())

                dbHelper.use {
                    val db = dbHelper.writableDatabase
                    val values = ContentValues().apply {
                        put("product_name", name)
                        put("price", price)
                        put("memo", memo)
                        put("Dday",Dday)
                    }

                    db.insert("product", null, values)
                }

                Log.d("TAG", newElement.toString())

                binding.productName.setText("")
                binding.productPrice.setText("")
                binding.memo.setText("")

                val imm: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

                binding.workTime.text = Dtime.toString()
                binding.workDay.text = Dday.toString()
            }
        }

        return binding.root

    }

    override fun onProductSelected(name: String, price: String) {
        binding.productName.setText(name)
        binding.productPrice.setText(price)
    }

    private fun get(apiURL: String, clientId: String, clientSecret: String): String {
        val url = URL(apiURL)
        val con = url.openConnection() as HttpURLConnection
        return try {
            con.requestMethod = "GET"
            con.setRequestProperty("X-Naver-Client-Id", clientId)
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret)

            val responseCode = con.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                readBody(con.inputStream)
            } else {
                readBody(con.errorStream)
            }
        } catch (e: IOException) {
            "API 요청과 응답 실패: ${e.message}"
        } finally {
            con.disconnect()
        }
    }

    private fun readBody(body: InputStream): String {
        val streamReader = InputStreamReader(body)
        return BufferedReader(streamReader).use { reader ->
            val responseBody = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                responseBody.append(line)
            }
            responseBody.toString()
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
