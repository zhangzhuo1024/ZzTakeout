package com.example.zztakeout.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zztakeout.R
import com.example.zztakeout.model.dao.AddressDao
import com.example.zztakeout.ui.adapter.AddressRvAdapter
import com.example.zztakeout.ui.view.RecycleViewDivider
import kotlinx.android.synthetic.main.activity_recepit_address.*

class RecepitAddressActivity : AppCompatActivity() {
    lateinit var addressRvAdapter:AddressRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recepit_address)
        ib_back.setOnClickListener{
            finish()
        }
        ll_add_address.setOnClickListener{
            val intent = Intent(this, AddOrEditAddressActivity::class.java)
            startActivity(intent)
        }
        rv_receipt_address.layoutManager = LinearLayoutManager(this)
        rv_receipt_address.addItemDecoration(RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL))
        addressRvAdapter = AddressRvAdapter(this)
        rv_receipt_address.adapter = addressRvAdapter
    }

    override fun onStart() {
        super.onStart()
        val addressDao = AddressDao(this)
        val queryAddressBean = addressDao.queryAddressBean() as ArrayList
        addressRvAdapter.setData(queryAddressBean)
    }
}
