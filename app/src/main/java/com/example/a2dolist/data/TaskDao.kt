package com.example.a2dolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTask(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_TITLE -> getTasksSortedByTitle(query, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, title")
    fun getTasksSortedByTitle(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()


}