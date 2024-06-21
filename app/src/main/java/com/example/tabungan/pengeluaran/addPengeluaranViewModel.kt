package com.example.tabungan.pengeluaran

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tabungan.database.room.AppDao
import com.example.tabungan.database.room.AppDatabase
import com.example.tabungan.database.room.AppEntity
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class addPengeluaranViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: AppDao = AppDatabase.getInstance(application.applicationContext).appDao()
    val pengeluaran: LiveData<List<AppEntity>> = dao.getAllPengeluaran()
    val totalPengeluaran: LiveData<Int?> = dao.getTotalPengeluaran()

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllPengeluaran()
        }
    }

    fun deleteSingleData(uid: Int): String {
        return try {
            viewModelScope.launch(Dispatchers.IO) {
                val pengeluaranEntity = dao.getPengeluaranById(uid) // Get the entity by uid
                pengeluaranEntity?.let { dao.deletePengeluaran(it) }
            }
            "OK"
        } catch (e: Exception) {
            "Error"
        }
    }


    fun updatePengeluaran(uid: Int, note: String, date: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val pengeluaranEntity = pengeluaran.value?.find { it.uid == uid }
            pengeluaranEntity?.let {
                it.keterangan = note
                it.tanggal = date
                it.jmlUang = price
                dao.updatePengeluaran(it)
            }
        }
    }

    fun addPengeluaran(type: String, note: String, date: String, price: Int) {
        val deferred = CompletableDeferred<Unit>()

        GlobalScope.launch(Dispatchers.IO) {
            val pengeluaran = AppEntity(
                tipe = type,
                keterangan = note,
                tanggal = date,
                jmlUang = price
            )

            try {
                dao.insertPengeluaran(pengeluaran)
                deferred.complete(Unit)
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }

        runBlocking {
            deferred.await() // Menunggu selesai pada thread IO
        }
    }
}
