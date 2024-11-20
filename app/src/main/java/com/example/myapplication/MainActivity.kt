package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

data class Contact(
    val name: String,
    val phone: String
)

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyAdapter

    private val contacts = ArrayList<Contact>()

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val name = intent.getStringExtra("name") ?: ""
                val phone = intent.getStringExtra("phone") ?: ""
                contacts.add(Contact(name, phone))
                myAdapter.notifyDataSetChanged()
            }
        }
    }

    class MyAdapter(
        private val data: ArrayList<Contact>
    ) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val tvName: TextView = view.findViewById(R.id.tvName)
            private val tvPhone: TextView = view.findViewById(R.id.tvPhone)
            private val imgDelete: ImageView = view.findViewById(R.id.imgDelete)

            fun bind(item: Contact, onDelete: (Contact) -> Unit) {
                tvName.text = item.name
                tvPhone.text = item.phone
                imgDelete.setOnClickListener { onDelete(item) }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_row, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(data[position]) { item ->
                data.remove(item)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val btnAdd: Button = findViewById(R.id.btnAdd)

        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        myAdapter = MyAdapter(contacts)
        recyclerView.adapter = myAdapter

        btnAdd.setOnClickListener {
            val intent = Intent(this, SecActivity::class.java)
            startForResult.launch(intent)
        }
    }
}
