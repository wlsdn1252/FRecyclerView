package com.example.frecyclerview

import androidx.room.Entity
import androidx.room.PrimaryKey

// 어댑터클래스에서 사용될 데이터 타입 정의?
// 홀딩할 클래스?
@Entity(tableName = "word")
data class Word(
    val text:String,
    val mean:String,
    val type:String,
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
)
