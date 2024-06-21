package com.example.tabungan.pemasukan

import android.os.Bundle
import android.util.Log
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
import androidx.appcompat.app.AlertDialog
import com.example.tabungan.R
import com.example.tabungan.database.room.AppEntity
import com.example.tabungan.pengeluaran.addPemasukanViewModel
import com.example.tabungan.utils.functionHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class pemasukanFragment : Fragment(), pemasukanAdapter.PemasukanAdapterCallback {

    private lateinit var PemasukanAdapter: pemasukanAdapter
    private lateinit var PemasukanViewModel: addPemasukanViewModel
    private var modelDatabaseList: MutableList<AppEntity> = mutableListOf()
    private lateinit var tvTotal: TextView
    private lateinit var tvNotFound: TextView
    private lateinit var btnHapus: Button
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var rvListData: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pemasukan, container, false)
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

    private fun initAdapter() {
        PemasukanAdapter = pemasukanAdapter(requireContext(), modelDatabaseList, this)
        rvListData.layoutManager = LinearLayoutManager(requireContext())
        rvListData.itemAnimator = DefaultItemAnimator()
        rvListData.adapter = PemasukanAdapter
    }

    private fun observeData() {
        PemasukanViewModel = ViewModelProvider(this).get(addPemasukanViewModel::class.java)

        PemasukanViewModel.pemasukan.observe(viewLifecycleOwner, Observer { pemasukan ->
            if (pemasukan == null) {
                Log.e("pemasukanFragment", "pemasukan is null")
                return@Observer
            }
            if (pemasukan.isEmpty()) {
                btnHapus.visibility = View.GONE
                tvNotFound.visibility = View.VISIBLE
                rvListData.visibility = View.GONE
                // Jika tidak ada pemasukan, atur total menjadi 0
                tvTotal.text = "0"
            } else {
                btnHapus.visibility = View.VISIBLE
                tvNotFound.visibility = View.GONE
                rvListData.visibility = View.VISIBLE
            }
            PemasukanAdapter.addData(pemasukan)
        })

        PemasukanViewModel.totalPemasukan.observe(viewLifecycleOwner, Observer { integer ->
            if (integer == null) {
                Log.e("pemasukanFragment", "totalPemasukan is null")
                return@Observer
            }
            val totalPrice = integer ?: 0
            val initPrice = functionHelper.rupiahFormat(totalPrice)
            tvTotal.text = initPrice
        })
    }

    private fun initAction() {
        fabAdd.setOnClickListener {
            addPemasukan.startActivity(requireActivity(), false, null)
        }

        btnHapus.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage("Hapus semua data pemasukan?")
            alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface, _ ->
                PemasukanViewModel.deleteAllData()
                // Setelah menghapus semua data, atur total menjadi 0
                tvTotal.text = "0"
            }
            alertDialogBuilder.setNegativeButton("Batal") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            alertDialogBuilder.create().show()
        }
    }

    override fun onEdit(modelDatabase: AppEntity) {
        addPemasukan.startActivity(requireActivity(), true, modelDatabase)
    }

    override fun onDelete(modelDatabase: AppEntity) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Hapus data ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface, _ ->
            val uid = modelDatabase.uid ?: return@setPositiveButton
            PemasukanViewModel.deleteSingleData(uid)
            Toast.makeText(requireContext(), "Data yang dipilih sudah dihapus", Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface, _ ->
            dialogInterface.cancel()
        }
        alertDialogBuilder.create().show()
    }
}
