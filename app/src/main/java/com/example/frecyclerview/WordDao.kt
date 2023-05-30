package com.example.frecyclerview

import androidx.room.*

// DataAccessObject DB와 관련된 Query문 작성을 위한 클래스
@Dao
interface WordDao {
    @Query("SELECT * from word ORDER BY id DESC")
    fun getAll():List<Word>

    @Query("SELECT * from word ORDER BY id DESC LIMIT 1")
    fun getLateStWord(): Word

    @Insert
    fun insert(word:Word)

    @Delete
    fun delete(word: Word)

    @Update
    fun update(word: Word)
}