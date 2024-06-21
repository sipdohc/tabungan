package com.example.tabungan.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Mendeklarasikan bahwa kelas ini adalah database Room dengan anotasi @Database
@Database(entities = [AppEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Mendeklarasikan fungsi abstrak yang mengembalikan objek DAO yang sesuai
    abstract fun appDao(): AppDao

    // Membuat companion object untuk mendapatkan instance database
    companion object {
        // Volatile digunakan agar nilai INSTANCE selalu terlihat ke semua thread
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Fungsi untuk mendapatkan instance database
        fun getInstance(context: Context): AppDatabase {
            // Memeriksa apakah INSTANCE sudah ada, jika tidak, maka baru dibuat
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Fungsi untuk membangun database jika INSTANCE null
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "finance_database"
            )
                // Jika ada perubahan skema yang tidak bisa di-migrate, maka akan dihapus dan dibuat ulang
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
