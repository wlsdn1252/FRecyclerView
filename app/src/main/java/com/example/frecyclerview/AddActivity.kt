package com.example.frecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.frecyclerview.databinding.ActivityAddBinding
import com.google.android.material.chip.Chip

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        binding.addButton.setOnClickListener {
            add()
        }
    }

    private fun initViews(){
        // 칩그룹에 들어가는 데이터를 위한 리스트
        val types = listOf("명사","동사","대명사","형용사","부사","감탄사","전치사","접속사")
        binding.typeChipGroup.apply {
            types.forEach{text ->
                addView(createChip(text))
            }
        }
    }

    //
    private fun createChip(text: String): Chip{
        return Chip(this).apply {
            setText(text)
            isCheckable = true
            isClickable = true
        }
    }

    // 추가버튼 눌렀을 때
    private fun add(){
        // 액티비티에 적혀있는 값들을 들고온다.
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanTextInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        val word = Word(text, mean, type)


        // 메인 UI쓰레드랑 겹치면 안된다. 쓰레드를 따로 생성
        Thread{
            AppDatabase.getInstance(this)?.wordDao()?.insert(word)
            // 이건 뭐죠??
            runOnUiThread{
                Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
            }
            finish()
        }.start()
        


    }

}