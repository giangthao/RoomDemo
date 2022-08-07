package com.tutorial.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    fun addsome()
    @Insert
    suspend fun  insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM `employee-table`")
    fun fetchAllEmployees():Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `employee-table` where id=:id")
    fun fetchEmployeesId(id: Int):Flow<EmployeeEntity>



}