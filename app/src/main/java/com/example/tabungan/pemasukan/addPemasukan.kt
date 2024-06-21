package com.example.tabungan.pemasukan

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.tabungan.MainActivity
import com.example.tabungan.R
import com.example.tabungan.database.room.AppEntity
import com.example.tabungan.pengeluaran.addPemasukanViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class addPemasukan : AppCompatActivity() {

    private var mIsEdit = false
    private var strId = 0

    private lateinit var toolbar: Toolbar
    private lateinit var etKeterangan: TextInputEditText
    private lateinit var etTanggal: TextInputEditText
    private lateinit var etJmlUang: TextInputEditText
    private lateinit var btnSimpan: Button
    private lateinit var AddPemasukanViewModel: addPemasukanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_data)

        toolbar = findViewById(R.id.toolbar)
        etKeterangan = findViewById(R.id.etKeterangan)
        etTanggal = findViewById(R.id.etTanggal)
        etJmlUang = findViewById(R.id.etJmlUang)
        btnSimpan = findViewById(R.id.btnSimpan)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        AddPemasukanViewModel = ViewModelProvider(this).get(addPemasukanViewModel::class.java)

        loadData()
        initAction()
    }

    private fun loadData() {
        mIsEdit = intent.getBooleanExtra(KEY_IS_EDIT, false)
        if (mIsEdit) {
            val pemasukan: AppEntity? = intent.getParcelableExtra(KEY_DATA)
            if (pemasukan != null) {
                strId = pemasukan.uid
                etKeterangan.setText(pemasukan.keterangan)
                etTanggal.setText(pemasukan.tanggal)
                etJmlUang.setText(pemasukan.jmlUang.toString())
            } else {
                Toast.makeText(this, "Data tidak valid untuk diedit", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun initAction() {
        etTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val strFormatDefault = "d MMMM yyyy"
                val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
                etTanggal.setText(simpleDateFormat.format(calendar.time))
            }

            DatePickerDialog(
                this@addPemasukan, date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSimpan.setOnClickListener {
            val strTipe = "pemasukan"
            val strKeterangan = etKeterangan.text.toString()
            val strTanggal = etTanggal.text.toString()
            val strJmlUang = etJmlUang.text.toString()

            if (strKeterangan.isEmpty() || strTanggal.isEmpty() || strJmlUang.isEmpty()) {
                Toast.makeText(
                    this@addPemasukan, "Ups, form tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (mIsEdit) {
                    AddPemasukanViewModel.updatePemasukan(strId, strKeterangan, strTanggal, strJmlUang.toInt())
                } else {
                    AddPemasukanViewModel.addPemasukan(strTipe, strKeterangan, strTanggal, strJmlUang.toInt())
                }
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            startActivity(Intent(this@addPemasukan, MainActivity::class.java))
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
    companion object {
        private const val KEY_IS_EDIT = "key_is_edit"
        private const val KEY_DATA = "key_data"

        @JvmStatic
        fun startActivity(context: Context, isEdit: Boolean, pemasukan: AppEntity?) {
            val intent = Intent(context, addPemasukan::class.java).apply {
                putExtra(KEY_IS_EDIT, isEdit)
                putExtra(KEY_DATA, pemasukan)
            }
            context.startActivity(intent)
        }
    }


}