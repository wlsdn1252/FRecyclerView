package com.example.frecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {
    private lateinit var binding : ActivityMainBinding
    // 어댑터 선언
    private lateinit var wordAdapter : WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecylerView()
        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let{
                startActivity(it)
            }
        }
    }


    private fun initRecylerView(){

        wordAdapter = WordAdapter(mutableListOf(),this)


        // 리사이클러뷰랑 어댑터 연결
        binding.wordRecyclerView.apply {
            //리사이클러뷰에 어댑터 연결
            adapter = wordAdapter

            // 레이아웃 메니저 설정
            layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)

            //디바이더활용을 위한 아이템데코레이션 선언
            // 리사이클러뷰의 아이템간의 줄 표시??
            val dividerItemDecoration = DividerItemDecoration(applicationContext,LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    
    // 어댑터 클래스의 클릭을 위한 인터페이스를 활용
    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.text}가 클릭", Toast.LENGTH_SHORT).show()
    }
}