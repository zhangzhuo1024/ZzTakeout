package com.example.zztakeout.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.RecepitAddressBean
import com.example.zztakeout.ui.activity.AddOrEditAddressActivity
import com.example.zztakeout.ui.activity.RecepitAddressActivity

class AddressRvAdapter(val recepitAddressActivity: RecepitAddressActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mDatas: List<RecepitAddressBean> = ArrayList<RecepitAddressBean>()

    fun setData(data: java.util.ArrayList<RecepitAddressBean>) {
        mDatas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val recepitAddressItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_receipt_address, parent, false)
        return recepitAddressItemHolder(recepitAddressItemView)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as recepitAddressItemHolder).bindData(mDatas[position])
    }

    inner class recepitAddressItemHolder(var item: View) : RecyclerView.ViewHolder(item) {

        val ivEdit: ImageView
        val tvname:TextView
        val tv_sex:TextView
        val tv_phone:TextView
        val tv_label:TextView
        val tv_address:TextView
        lateinit var address :RecepitAddressBean

        init {
            ivEdit = itemView.findViewById(R.id.iv_edit) as ImageView
            ivEdit.setOnClickListener{
                val intent = Intent(recepitAddressActivity, AddOrEditAddressActivity::class.java)
                intent.putExtra("addressBean", address)
                recepitAddressActivity.startActivity(intent)
            }
            tvname = itemView.findViewById(R.id.tv_name) as TextView
            tv_sex = itemView.findViewById(R.id.tv_sex) as TextView
            tv_phone = itemView.findViewById(R.id.tv_phone) as TextView
            tv_label = itemView.findViewById(R.id.tv_label) as TextView
            tv_address = itemView.findViewById(R.id.tv_address) as TextView
            itemView.setOnClickListener {
                val data = Intent()
                data.putExtra("address", address)
                recepitAddressActivity.setResult(200, data)
                recepitAddressActivity.finish()
            }
        }

        fun bindData(recepitAddress: RecepitAddressBean) {
            this.address = recepitAddress
            tvname.text = recepitAddress.username
            tv_phone.text = recepitAddress.phone
            tv_sex.text = recepitAddress.sex
            tv_address.text = "${recepitAddress.address},${recepitAddress.detailAddress}"
            tv_label.text = recepitAddress.label
        }
    }
}
