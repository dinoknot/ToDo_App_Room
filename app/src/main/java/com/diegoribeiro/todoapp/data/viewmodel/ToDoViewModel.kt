package com.diegoribeiro.todoapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoribeiro.todoapp.data.ToDoDatabase
import com.diegoribeiro.todoapp.data.models.ToDoData
import com.diegoribeiro.todoapp.data.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val repository: ToDoRepository
): ViewModel() {

    private var _showReviewDialog = MutableLiveData<Boolean>()
    val showReviewDialog: LiveData<Boolean> = _showReviewDialog

    fun checkIfShouldShowFeedback() {
        _showReviewDialog.value = repository.shouldShowFeedbackDialog()
    }

    fun updateDialogShown() {
        repository.updateTimeReviewDialog()
    }

    
    var taskId = MutableLiveData<Int>()

    val getAllData: LiveData<List<ToDoData>>
    val sortByHighPriority: LiveData<List<ToDoData>>
    val sortByLowPriority: LiveData<List<ToDoData>>
    val sortByDateTime: LiveData<List<ToDoData>>

    init {
        taskId
        repository
        getAllData = repository.getAllData
        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
        sortByDateTime = repository.sortByDateTime
    }

    fun insert(toDoData: ToDoData){
       viewModelScope.launch(Dispatchers.IO) {
            repository.insert(toDoData)
            taskId.postValue(toDoData.id)
       }
    }

    fun updateData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateData(toDoData)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun deleteItem(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteData(toDoData)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>{
        return repository.searchDatabase(searchQuery)
    }
}