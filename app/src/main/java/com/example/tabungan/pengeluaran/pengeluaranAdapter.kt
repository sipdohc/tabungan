package com.example.tabungan.pengeluaran

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tabungan.R
import com.example.tabungan.database.room.AppEntity
import com.example.tabungan.utils.functionHelper

class pengeluaranAdapter(
    private val context: Context,
    private var list: List<AppEntity>,
    private val mAdapterCallback: PengeluaranAdapterCallback
) : RecyclerView.Adapter<pengeluaranAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lis_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelDatabase = list[position]
        holder.bindData(modelDatabase)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun clear() {
        val size = this.list.size
        this.list = emptyList()
        notifyItemRangeRemoved(0, size)
    }

    fun addData(pengeluarans: List<AppEntity>) {
        this.list = pengeluarans
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)

        init {
            itemView.setOnClickListener {
                val pengeluaran = list[adapterPosition]
                mAdapterCallback.onEdit(pengeluaran)
            }

            ivDelete.setOnClickListener {
                val pengeluaran = list[adapterPosition]
                mAdapterCallback.onDelete(pengeluaran)
            }
        }

        fun bindData(item: AppEntity) {
            val price = item.jmlUang
            val initPrice = functionHelper.rupiahFormat(price)
            tvPrice.text = initPrice

            val note = item.keterangan
            tvNote.text = note

            val date = item.tanggal
            tvDate.text = date
        }
    }

    interface PengeluaranAdapterCallback {
        fun onEdit(modelDatabase: AppEntity)
        fun onDelete(modelDatabase: AppEntity)
    }
}
