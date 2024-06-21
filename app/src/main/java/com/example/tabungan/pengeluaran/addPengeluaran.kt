package com.example.tabungan.pengeluaran

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
import androidx.lifecycle.get
import com.example.tabungan.MainActivity
import com.example.tabungan.R
import com.example.tabungan.database.room.AppEntity
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class addPengeluaran : AppCompatActivity() {

    private var mIsEdit = false
    private var strUid = 0

    private lateinit var toolbar: Toolbar
    private lateinit var etKeterangan: TextInputEditText
    private lateinit var etTanggal: TextInputEditText
    private lateinit var etJmlUang: TextInputEditText
    private lateinit var btnSimpan: Button
    private lateinit var AddPengeluaranViewModel: addPengeluaranViewModel

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

        AddPengeluaranViewModel = ViewModelProvider(this).get(addPengeluaranViewModel::class.java)

        loadData()
        initAction()
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
                this@addPengeluaran, date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSimpan.setOnClickListener {
            val strTipe = "pengeluaran"
            val strKeterangan = etKeterangan.text.toString()
            val strTanggal = etTanggal.text.toString()
            val strJmlUang = etJmlUang.text.toString()

            if (strKeterangan.isEmpty() || strJmlUang.isEmpty()) {
                Toast.makeText(
                    this@addPengeluaran, "Ups, form tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (mIsEdit) {
                    AddPengeluaranViewModel.updatePengeluaran(strUid, strKeterangan, strTanggal, strJmlUang.toInt())
                } else {
                    AddPengeluaranViewModel.addPengeluaran(strTipe, strKeterangan, strTanggal, strJmlUang.toInt())
                }
                finish()
            }
        }
    }

    private fun loadData() {
        mIsEdit = intent.getBooleanExtra(KEY_IS_EDIT, false)
        if (mIsEdit) {
            val pengeluaran: AppEntity? = intent.getParcelableExtra(KEY_DATA)
            pengeluaran?.let {
                strUid = it.uid
                etKeterangan.setText(it.keterangan)
                etTanggal.setText(it.tanggal)
                etJmlUang.setText(it.jmlUang.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            startActivity(Intent(this@addPengeluaran, MainActivity::class.java))
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val KEY_IS_EDIT = "key_is_edit"
        private const val KEY_DATA = "key_data"

        @JvmStatic
        fun startActivity(context: Context, isEdit: Boolean, pengeluaran: AppEntity?) {
            val intent = Intent(context, addPengeluaran::class.java).apply {
                putExtra(KEY_IS_EDIT, isEdit)
                putExtra(KEY_DATA, pengeluaran)
            }
            context.startActivity(intent)
        }
    }

}
