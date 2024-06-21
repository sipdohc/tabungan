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

class addPemasukanViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: AppDao = AppDatabase.getInstance(application.applicationContext).appDao()
    val pemasukan: LiveData<List<AppEntity>> = dao.getAllPemasukan()
    val totalPemasukan: LiveData<Int?> = dao.getTotalPemasukan()

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllPemasukan()
        }
    }

    fun deleteSingleData(uid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pemasukanEntity = dao.getPemasukanById(uid)
            pemasukanEntity?.let { dao.deletePemasukan(it) }
        }
    }

    fun updatePemasukan(uid: Int, note: String, date: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pemasukanEntity = dao.getPemasukanById(uid)
            pemasukanEntity?.let {
                it.keterangan = note
                it.tanggal = date
                it.jmlUang = price
                dao.updatePemasukan(it)
            }
        }
    }

    fun addPemasukan(type: String, note: String, date: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pemasukan = AppEntity(
                tipe = type,
                keterangan = note,
                tanggal = date,
                jmlUang = price
            )
            dao.insertPemasukan(pemasukan)
        }
    }
}
