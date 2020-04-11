package com.example.zztakeout.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.RecepitAddressBean
import kotlinx.android.synthetic.main.activity_confirm_order.*

class ConfirmOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        rl_location.setOnClickListener{
            val intent = Intent(this, RecepitAddressActivity::class.java)
            startActivityForResult(intent,1001)
        }
        tvSubmit.setOnClickListener{
            val intent = Intent(this, OnlinePaymentActivity::class.java)
            startActivityForResult(intent,1002)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == 200) {
            if (data != null){
                val recepitAddressBean = data.getSerializableExtra("address") as RecepitAddressBean
                tv_name.text = recepitAddressBean.username
                tv_phone.text = recepitAddressBean.phone
                tv_label.text = recepitAddressBean.label
                tv_address.text = recepitAddressBean.address

            }
        }
    }
}
