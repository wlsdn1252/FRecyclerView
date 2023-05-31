package com.example.frecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {
    private lateinit var binding : ActivityMainBinding
    // 어댑터 선언
    private lateinit var wordAdapter : WordAdapter
    // 리스트뷰의 한 아이템을 클릭했을 떄의 변수
    private var selectedWord: Word? = null

    // 현재 주기상태는 resume상태일것이다.
    // 값을 추가하러 추가하는 액티비티에 할 때마다 DB검사 후 UI에 뿌려주면 효과적이지 못함
    // 그래서 값을 추가하는 액티비티로 넘어간 후 추가 버튼을 클릭했을 때 DB를 읽고 UI에 새롭게 뿌려줄거다
    private val updateAddWordResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        // 만약 DB의 데이터가 추가되었다면 가장 나중에 추가된 값을 가져오자
        val isUpdated = result.data?.getBooleanExtra("isUpdate",false) ?: false

        // 데이터를 추가하고 추가버튼을 눌렀는지 확인 후 데이터가 추가되었다면
        if(result.resultCode == RESULT_OK && isUpdated){
            updateAddWord()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecylerView()
        binding.addButton.setOnClickListener {

            Intent(this, AddActivity::class.java).let{
                // startActivity대신 사용한다.
                updateAddWordResult.launch(it)
            }
        }

        binding.deleteImageView.setOnClickListener {
            delete()
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

        Thread{
            // db에 있는 데이터 들고오기
            // 리사이클러뷰에 뿌릴거임
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()

            //어댑터에 db에서 들고온 데이터 연결
            wordAdapter.list.addAll(list)

            // notifyDataSetChanged()를 사용할 땐 어댑터의 새로운 내룡이 그려져 UI가 변경된다.
            // 그러므로 runOnUiThread를 사용한다.
            runOnUiThread {
                // 어댑터의 데이터의 변화가 있는지 확인 후 화면에 뿌려준다.
                wordAdapter.notifyDataSetChanged()
            }

        }.start()


    }

    // db에 데이터가 추가 되었다면
    private fun updateAddWord(){

        // db에 접근하려면 쓰레드 활용
        Thread{
            // db에서 값 들고오고
           AppDatabase.getInstance(this)?.wordDao()?.getLateStWord()?.let { word ->
               // 어댑터에 리스트형태로 add하는데 가장 최신의 데이터를 들고올거라서 인덱스값을 0으로 준다.
               wordAdapter.list.add(0,word)

               // 데이터의 변화가 있으니 어댑터를 다시 ui에 로드한다.
               runOnUiThread { wordAdapter.notifyDataSetChanged() }
           }

        }.start()
    }

    // 제거버튼 클릭 시
    private fun delete(){
        // 만약 아이템을 선택하지 않고 사젝버튼을 눌렀을 때
        // 아무일도 일어나지 않는다.
        if(selectedWord == null) return

        Thread{
            selectedWord?.let{word ->
                //DB에 접근하여 selectedWord가 있는 아이템을??
                AppDatabase.getInstance(this)?.wordDao()?.delete(word)
                // 데이터의 변화가 있으니 어댑터를 다시 ui에 로드한다.
                runOnUiThread {
                    // 어댑터에서도 해당 데이터를 삭제시킨다.
                    wordAdapter.list.remove(word)
                    wordAdapter.notifyDataSetChanged()
                    // 단순 목록 초기화
                    binding.textTextView.text = ""
                    binding.meanTextView.text = ""
                    Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
                }


            }

        }.start()
    }

    
    // 어댑터 클래스의 클릭을 위한 인터페이스를 활용
    override fun onClick(word: Word) {
        // 클릭된 한 아이템의 정보를 받아와
        selectedWord = word
        // 뿌러준다.
        binding.textTextView.text = word.text
        binding.meanTextView.text = word.mean

    }
}