package com.tutorial.roomdemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.R
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tutorial.roomdemo.databinding.ActivityMainBinding
import com.tutorial.roomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener {
            // TODO call addrecord with employeeDao
            addRecord(employeeDao)
        }
        lifecycleScope.launch{
            employeeDao.fetchAllEmployees().collect{
                Log.d("exactemployee","$it")
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list,employeeDao)
            }
        }
    }
    fun addRecord(employeeDao: EmployeeDao){
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()
        if(email.isNotEmpty() && name.isNotEmpty()){
            lifecycleScope.launch{
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext,"Record saved",Toast.LENGTH_LONG)
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Name or Email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    /**
     * Function is used show the list of inserted data.
     */
    private fun setupListOfDataIntoRecyclerView(employeesList:ArrayList<EmployeeEntity>,
                                                employeeDao: EmployeeDao) {

        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeesList,
                {
                    updateId ->
                    updateRecordDialog(updateId, employeeDao)
                },
                {
                    deleteId ->
                    lifecycleScope.launch {
                        employeeDao.fetchEmployeesId(deleteId).collect {
                            if (it != null) {
                                deleteRecordAlertDialog(deleteId, employeeDao,it)
                            }
                        }
                    }
                })


            // Set the LayoutManager that this RecyclerView will use.
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            // adapter instance is set to the recyclerview to inflate the items.
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {

            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }
    /** Todo 6
     * Method is used to show the Alert Dialog and delete the selected employee.
     * We add an id to get the selected position and an employeeDao param to get the
     * methods from the dao interface then launch a coroutine block to call the methods
     */

    private fun deleteRecordAlertDialog(it: Int, employeeDao: EmployeeDao, employeeEntity: EmployeeEntity) {
        val dialog =AlertDialog.Builder(this)
        //set title for alert Dialog
        dialog.setTitle("Delete Record")
        // set message for alert Dialog
        lifecycleScope.launch{
            employeeDao.fetchEmployeesId(it).collect{
                if(it!=null){
                    dialog.setMessage("Are you sure you wants to delete ${it.name}")
                }
            }
        }
        dialog.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        dialog.setPositiveButton("Yes"){
            dialogInterface, _->
            lifecycleScope.launch{
                //calling the deleteEmployee method of Database Handler class to delete record
                employeeDao.delete(EmployeeEntity(it))
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
                dialogInterface.dismiss()
            }
        }
            //performing negative action
        dialog.setNegativeButton("No"){
            dialogInterface, which -> dialogInterface.dismiss()
        }
        //Create the AlertDialog
        val alertDialog:AlertDialog = dialog.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show() // show the dialog to UI

    }

    private fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val dialog = Dialog(this, R.style.Theme_AppCompat_Dialog)
        // Will not allow user to cancel after click remaining screen
        dialog.setCancelable(false)
        /*Set the screen content from a layout resource.
        * The resource will be inflated, adding all top-level views to the screen
        * */
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        lifecycleScope.launch {
            employeeDao.fetchEmployeesId(id).collect(){
                if (it!=null){
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                }
            }
        }
        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()
            if(name.isNotEmpty() && email.isNotEmpty()){
                lifecycleScope.launch{
                    employeeDao.update(EmployeeEntity(id,name,email))
                    Toast.makeText(applicationContext,"Recored Updated",Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }

            }
            else {
                Toast.makeText(applicationContext,
                "Name or Email cannot be blank",
                Toast.LENGTH_LONG
                    )
            }
        }
        binding.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}