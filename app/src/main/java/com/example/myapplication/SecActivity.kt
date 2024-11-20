package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sec)

        // 設置視圖邊界處理
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化元件
        val edName: EditText = findViewById(R.id.edName)
        val edPhone: EditText = findViewById(R.id.edPhone)
        val btnSend: Button = findViewById(R.id.btnSend)

        // 設定按鈕監聽器
        btnSend.setOnClickListener {
            validateAndSendData(edName.text.toString(), edPhone.text.toString())
        }
    }

    /**
     * 驗證輸入的資料並回傳結果
     */
    private fun validateAndSendData(name: String, phone: String) {
        when {
            name.isEmpty() -> showToast("請輸入姓名")
            phone.isEmpty() -> showToast("請輸入電話")
            else -> {
                // 將資料存入 Bundle 並回傳
                val bundle = Bundle().apply {
                    putString("name", name)
                    putString("phone", phone)
                }
                setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
                finish()
            }
        }
    }

    /**
     * 顯示 Toast 訊息
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
