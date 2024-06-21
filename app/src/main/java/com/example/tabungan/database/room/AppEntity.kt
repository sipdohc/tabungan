package com.example.tabungan.database.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_keuangan")
data class AppEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,

    @ColumnInfo(name = "tipe")
    val tipe: String,

    @ColumnInfo(name = "keterangan")
    var keterangan: String,

    @ColumnInfo(name = "jml_uang")
    var jmlUang: Int,

    @ColumnInfo(name = "tanggal")
    var tanggal: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(tipe)
        parcel.writeString(keterangan)
        parcel.writeInt(jmlUang)
        parcel.writeString(tanggal)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AppEntity> {
        override fun createFromParcel(parcel: Parcel): AppEntity {
            return AppEntity(parcel)
        }

        override fun newArray(size: Int): Array<AppEntity?> {
            return arrayOfNulls(size)
        }
    }
}
