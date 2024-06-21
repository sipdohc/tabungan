package com.example.tabungan.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDao {
    @Query("SELECT * FROM tbl_keuangan WHERE tipe = 'pengeluaran' ORDER BY uid DESC")
    fun getAllPengeluaran(): LiveData<List<AppEntity>>

    @Query("SELECT * FROM tbl_keuangan WHERE uid = :uid")
    fun getPengeluaranById(uid: Int): AppEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPengeluaran(pengeluaran: AppEntity)

    @Update
    fun updatePengeluaran(pengeluaran: AppEntity)

    @Delete
    fun deletePengeluaran(pengeluaran: AppEntity)

    @Query("DELETE FROM tbl_keuangan WHERE tipe = 'pengeluaran'")
    fun deleteAllPengeluaran()

    @Query("SELECT SUM(jml_uang) FROM tbl_keuangan WHERE tipe = 'pengeluaran'")
    fun getTotalPengeluaran(): LiveData<Int?>

    @Query("SELECT * FROM tbl_keuangan WHERE tipe = 'pemasukan' ORDER BY uid DESC")
    fun getAllPemasukan(): LiveData<List<AppEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPemasukan(pemasukan: AppEntity)

    @Update
    fun updatePemasukan(pemasukan: AppEntity)

    @Delete
    fun deletePemasukan(pemasukan: AppEntity)

    @Query("DELETE FROM tbl_keuangan WHERE tipe = 'pemasukan'")
    fun deleteAllPemasukan()

    @Query("SELECT SUM(jml_uang) FROM tbl_keuangan WHERE tipe = 'pemasukan'")
    fun getTotalPemasukan(): LiveData<Int?>

    @Query("SELECT * FROM tbl_keuangan WHERE uid = :uid")
    fun getPemasukanById(uid: Int): AppEntity?
}
