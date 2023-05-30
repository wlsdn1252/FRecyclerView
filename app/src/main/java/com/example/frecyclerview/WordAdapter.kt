package com.example.frecyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// 리사이클러뷰.어댑터를 상속받을 때 <>안에 뷰홀더를 받아야함
// 그래서 이너클래스로 뷰홀더를 만들었다.
// 이너클래스라서 사용할 때는 클래스이름.클래스이름을 쓴다.
// WordAdapter는 Word데이터클래스를 변경가능한 리스트 형태로 받는다.
class WordAdapter(val list: MutableList<Word>) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {


    //WordAdapter클래스가 들고있는 list의 데이터 개수를 알려준다.
    // 즉 Word데이터 클래스의에 들어있는 데이터 수???
    override fun getItemCount(): Int {
        return list.size
    }

    // 뷰홀더를 만들 때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {

    }


    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    // 뷰홀더클래스의 속성은 리사이클러뷰.뷰홀더를 상속받는다.
    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

}