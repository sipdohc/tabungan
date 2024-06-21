package com.example.tabungan.pengeluaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tabungan.database.room.AppDao
import com.example.tabungan.database.room.AppDatabase
import com.example.tabungan.database.room.AppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class pengeluaranViewModel(application: Application) : AndroidViewModel(application) {

    private val appDao: AppDao = AppDatabase.getInstance(application).appDao()
    val pengeluaran: LiveData<List<AppEntity>> = appDao.getAllPengeluaran()
    val totalPengeluaran: LiveData<Int?> = appDao.getTotalPengeluaran()

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.deleteAllPengeluaran()
        }
    }

    fun deleteSingleData(uid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pengeluaranEntity = appDao.getPengeluaranById(uid)
            pengeluaranEntity?.let { appDao.deletePengeluaran(it) }
        }
    }

}
