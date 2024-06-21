package com.example.tabungan.pengeluaran

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tabungan.R
import com.example.tabungan.database.room.AppEntity
import com.example.tabungan.utils.functionHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

class pengeluaranFragment : Fragment(), pengeluaranAdapter.PengeluaranAdapterCallback {

    private lateinit var pengeluaranAdapter: pengeluaranAdapter
    private lateinit var pengeluaranViewModel: addPengeluaranViewModel
    private val modelDatabase: MutableList<AppEntity> = ArrayList()
    private lateinit var tvTotal: TextView
    private lateinit var tvNotFound: TextView
    private lateinit var btnHapus: Button
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var rvListData: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pengeluaran, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTotal = view.findViewById(R.id.tvTotal)
        tvNotFound = view.findViewById(R.id.tvNotFound)
        btnHapus = view.findViewById(R.id.btnHapus)
        fabAdd = view.findViewById(R.id.fabAdd)
        rvListData = view.findViewById(R.id.rvListData)

        tvNotFound.visibility = View.GONE

        initAdapter()
        observeData()
        initAction()
    }

    private fun initAction() {
        fabAdd.setOnClickListener {
            addPengeluaran.startActivity(requireActivity(), false, null)
        }

        btnHapus.setOnClickListener {
            pengeluaranViewModel.deleteAllData()
            tvTotal.text = "0"
        }
    }

    private fun initAdapter() {
        pengeluaranAdapter = pengeluaranAdapter(requireContext(), modelDatabase, this)
        rvListData.layoutManager = LinearLayoutManager(requireContext())
        rvListData.itemAnimator = DefaultItemAnimator()
        rvListData.adapter = pengeluaranAdapter
    }

    private fun observeData() {
        pengeluaranViewModel = ViewModelProvider(this).get(addPengeluaranViewModel::class.java)
        pengeluaranViewModel.pengeluaran.observe(viewLifecycleOwner, Observer { pengeluaran ->
            if (pengeluaran.isEmpty()) {
                btnHapus.visibility = View.GONE
                tvNotFound.visibility = View.VISIBLE
                rvListData.visibility = View.GONE
            } else {
                btnHapus.visibility = View.VISIBLE
                tvNotFound.visibility = View.GONE
                rvListData.visibility = View.VISIBLE
            }
            pengeluaranAdapter.addData(pengeluaran)
        })

        pengeluaranViewModel.totalPengeluaran.observe(viewLifecycleOwner, Observer { integer ->
            val totalPrice = integer ?: 0
            val initPrice = functionHelper.rupiahFormat(totalPrice)
            tvTotal.text = initPrice
        })
    }

    override fun onEdit(modelDatabase: AppEntity) {
        addPengeluaran.startActivity(requireActivity(), true, modelDatabase)
    }

    override fun onDelete(modelDatabase: AppEntity) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Hapus riwayat ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface, i ->
            val uid = modelDatabase.uid
            val sKeterangan = pengeluaranViewModel.deleteSingleData(uid)
            if (sKeterangan == "OK") {
                Toast.makeText(requireContext(), "Data yang dipilih sudah dihapus", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface, i -> dialogInterface.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}
