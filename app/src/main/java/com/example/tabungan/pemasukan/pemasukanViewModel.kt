package com.example.tabungan.pemasukan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tabungan.database.room.AppDao
import com.example.tabungan.database.room.AppDatabase
import com.example.tabungan.database.room.AppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class pemasukanViewModel(application: Application) : AndroidViewModel(application) {

    private val appDao: AppDao = AppDatabase.getInstance(application).appDao()
    val pemasukan: LiveData<List<AppEntity>> = appDao.getAllPemasukan()
    val totalPemasukan: LiveData<Int?> = appDao.getTotalPemasukan()

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.deleteAllPemasukan()
        }
    }

    fun deleteSingleData(uid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pemasukanEntity = appDao.getPemasukanById(uid)
            pemasukanEntity?.let { appDao.deletePemasukan(it) }
        }
    }
}
