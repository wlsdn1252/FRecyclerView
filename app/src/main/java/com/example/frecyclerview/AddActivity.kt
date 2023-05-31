package com.example.frecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.children
import com.example.frecyclerview.databinding.ActivityAddBinding
import com.google.android.material.chip.Chip

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var originWord : Word? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        binding.addButton.setOnClickListener {
            //추가버튼 클릭시 기존 데이터에 값이 없으면 추가 있으면 수정하겠다.
            if(originWord == null) add() else edit()

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

        // getExtra로 받아와서 기존에 있던 데이터 화면에 뿌리기
        originWord = intent.getParcelableExtra("originWord")
        originWord?.let { word ->
            binding.textInputEditText.setText(word.text)
            binding.meanTextInputEditText.setText(word.mean)
            val selectedChip = binding.typeChipGroup.children.firstOrNull{(it as Chip).text == word.type}as? Chip
            selectedChip?.isCheckable = true
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
            // 낭비최소화를 위한구문
            // 데이터입력 후 저장버튼을 누르면
            val intent = Intent().putExtra("isUpdate",true)
            setResult(RESULT_OK,intent)
            finish()
        }.start()
    }

    // DB값 수정할 떄
    private fun edit(){
        // 기존 DB에 저장되있던 데이터를 들고온다.
        val text = binding.textInputEditText.text.toString()
        val mean = binding.meanTextInputEditText.text.toString()
        val type = findViewById<Chip>(binding.typeChipGroup.checkedChipId).text.toString()
        // 값을 똑같이 받아온 후 해당 값을 변경한다.
        val editword = originWord?.copy(text = text, mean = mean, type = type)

        Thread{
            editword?.let {word ->
                AppDatabase.getInstance(this)?.wordDao()?.update(word)
                val intent = Intent().putExtra("editWord",editword)
                setResult(RESULT_OK,intent)
                runOnUiThread { Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show() }
                finish()
            }
        }.start()

    }

}