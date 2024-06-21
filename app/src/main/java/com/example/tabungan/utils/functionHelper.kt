package com.example.tabungan.utils

import java.text.DecimalFormat

object functionHelper {

    fun rupiahFormat(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return "Rp " + formatter.format(price).replace(",", ".")
    }
}