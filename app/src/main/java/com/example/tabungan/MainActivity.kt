package com.example.tabungan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.tabungan.database.api.APIConfig
import com.example.tabungan.database.api.Response
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as ApiResponse

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var recyclerView: RecyclerView
    private lateinit var Adapter: adapter
    private val tabIcons = arrayOf(R.drawable.ic_pengeluaran, R.drawable.ic_pemasukan)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi tampilan
        setIdLayout()
        setInitLayout()
        initRecyclerView()

        // Ambil data dari API
        fetchDataFromApi()
    }

    private fun setIdLayout() {
        tabLayout = findViewById(R.id.tabsLayout)
        viewPager = findViewById(R.id.viewPager)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun setInitLayout() {
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = 2
        tabLayout.setupWithViewPager(viewPager)

        // Atur icon untuk tab
        tabLayout.getTabAt(0)?.setIcon(tabIcons[0])
        tabLayout.getTabAt(1)?.setIcon(tabIcons[1])
    }

    private fun initRecyclerView() {
        val dataList = emptyList<Response>() // Data awal kosong
        Adapter = adapter(dataList)
        recyclerView.adapter = Adapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
    }

    private fun fetchDataFromApi() {
        val apiService = APIConfig.getApiService()
        val call = apiService.getUser()

        call.enqueue(object : Callback<List<Response>> {
            override fun onResponse(call: Call<List<Response>>, response: ApiResponse<List<Response>>) {
                if (response.isSuccessful) {
                    val dataList = response.body() ?: emptyList()
                    Adapter = adapter(dataList)
                    recyclerView.adapter = Adapter
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<List<Response>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
