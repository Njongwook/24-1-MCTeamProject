package com.example.timeconversionapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class ListPlaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_place)

        val submitButton: Button = findViewById(R.id.submit)
        submitButton.setOnClickListener {
            // 버튼 클릭 시 시간 등록 Activity로 이동하는 코드
            val intent = Intent(this@ListPlaceActivity, JobRegisDetailActivity::class.java)
            startActivity(intent)
        }

        // 0000.00.00 - 00.00 부분에 저장된 기간 정보 넣는 코드 작성해야 합니다.
        // 기간 동안 일했던 금액의 총합 ₩00,000 부분에 넣는 코드 작성해야 합니다.
    }
}

