package com.example.zztakeout.ui.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.zztakeout.R
import com.example.zztakeout.ui.activity.LoginActivity
import com.example.zztakeout.utils.TakeoutApplication

class UserFragment : Fragment() {
    lateinit var ll_userinfo:LinearLayout
    lateinit var username:TextView
    lateinit var phone:TextView
    lateinit var ivLogin:ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val userview = inflater.inflate(R.layout.fragment_user, container, false)
        ll_userinfo = userview.findViewById<LinearLayout>(R.id.ll_userinfo)
        username = userview.findViewById<TextView>(R.id.username)
        phone = userview.findViewById<TextView>(R.id.phone)
        ivLogin = userview.findViewById<ImageView>(R.id.login)
        ivLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
        return userview
    }

    override fun onStart() {
        super.onStart()
        val user = TakeoutApplication.sUser
        Log.e("Takeout", " UserFragment user.id" + user.id)
        if (user.id == -1) {
            ll_userinfo.visibility = View.GONE
            ivLogin.visibility = View.VISIBLE
        } else {
            username.text =  "欢迎您，${user.name}"
            phone.text = user.phone
            ll_userinfo.visibility = View.VISIBLE
            ivLogin.visibility = View.GONE
        }
    }
}
