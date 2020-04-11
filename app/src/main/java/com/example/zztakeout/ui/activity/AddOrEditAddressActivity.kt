package com.example.zztakeout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.RecepitAddressBean
import com.example.zztakeout.model.dao.AddressDao
import kotlinx.android.synthetic.main.activity_add_or_edit_address.*

class AddOrEditAddressActivity : AppCompatActivity() {
    lateinit var addressDao: AddressDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_address)
        processIntent()
        ib_back.setOnClickListener { finish() }
        ib_select_label.setOnClickListener { selectLabel() }
        btn_ok.setOnClickListener { processAdress() }
        btn_location_address.setOnClickListener{openMapAndLocation()}
        addressDao = AddressDao(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if(resultCode == 200 && intent != null){
            val title = intent.getStringExtra("title")
            val address = intent.getStringExtra("address")
            et_receipt_address.setText(title)
            et_detail_address.setText(address)
        }

    }

    private fun openMapAndLocation() {
        val intent = Intent(this, MapLocationActivity::class.java)
        startActivityForResult(intent, 1003)
    }

    private fun processAdress() {
        if (intent.hasExtra("addressBean")) {
            updateAddress()
        } else {
            tv_title.text = "新增地址"
            insertAddress()

        }
    }

    lateinit var addressBean:RecepitAddressBean
    private fun processIntent() {
        if (intent.hasExtra("addressBean")) {
            tv_title.text = "修改地址"
            addressBean = intent.getSerializableExtra("addressBean") as RecepitAddressBean
            ib_delete.visibility = View.VISIBLE
            ib_delete.setOnClickListener{
                addressDao.deleteAddressBean(addressBean)
                Toast.makeText(this, "地址删除成功！", Toast.LENGTH_SHORT).show()
                finish()
            }
            et_name.setText(addressBean.username)
            val sex1 = addressBean.sex
            if (sex1.equals("先生")){
                rb_man.isChecked = true
                rb_women.isChecked = false
            } else {
                rb_man.isChecked = false
                rb_women.isChecked = true
            }
            et_phone.setText(addressBean.phone)
            et_receipt_address.setText(addressBean.address)
            et_detail_address.setText(addressBean.detailAddress)
            tv_label.setText(addressBean.label)
        } else {
            tv_title.text = "新增地址"
        }
    }

    private fun insertAddress() {
        var username = et_name.text.toString().trim()
        var sex = "女士"
        if (rb_man.isChecked) {
            sex = "先生"
        }
        var phone = et_phone.text.toString().trim()
        var phoneOther = et_phone_other.text.toString().trim()
        var address = et_receipt_address.text.toString().trim()
        var detailAddress = et_detail_address.text.toString().trim()
        var label = tv_label.text.toString()
        if (!checkReceiptAddressInfo()) {
            return
        }
        addressDao.addAddressBean(RecepitAddressBean(1, username, sex, phone, address, detailAddress, label, "38"))
        Toast.makeText(this, "地址保存成功！", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun updateAddress() {
        var sex = "女士"
        if (rb_man.isChecked) {
            sex = "先生"
        }
        var username = et_name.text.toString().trim()
        var phone = et_phone.text.toString().trim()
        var phoneOther = et_phone_other.text.toString().trim()
        var address = et_receipt_address.text.toString().trim()
        var detailAddress = et_detail_address.text.toString().trim()
        var label = tv_label.text.toString()
        if (!checkReceiptAddressInfo()) {
            return
        }
        addressBean.username = username
        addressBean.sex = sex
        addressBean.phone = phone
        addressBean.address = address
        addressBean.detailAddress = detailAddress
        addressBean.label = label
        addressDao.updateAddressBean(addressBean)
        Toast.makeText(this, "地址保存成功！", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun selectLabel() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("请选择标签")
        val arrayOfLable = arrayOf("无", "家", "公司")
        builder.setItems(arrayOfLable) { dialog, which ->
            tv_label.text = arrayOfLable[which].toString()
            dialog.dismiss()
        }
        builder.show()
    }

    fun checkReceiptAddressInfo(): Boolean {
        val name = et_name.getText().toString().trim()
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show()
            return false
        }
        val phone = et_phone.getText().toString().trim()
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isMobileNO(phone)) {
            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show()
            return false
        }
        val receiptAddress = et_receipt_address.getText().toString().trim()
        if (TextUtils.isEmpty(receiptAddress)) {
            Toast.makeText(this, "请填写收获地址", Toast.LENGTH_SHORT).show()
            return false
        }
        val address = et_detail_address.getText().toString().trim()
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isMobileNO(phone: String): Boolean {
        val telRegex = "[1][358]\\d{9}"//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return phone.matches(telRegex.toRegex())
    }
}
