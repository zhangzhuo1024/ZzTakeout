package com.example.zztakeout.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.zztakeout.R
import com.example.zztakeout.ui.activity.LoginActivity

class UserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val userview = inflater.inflate(R.layout.fragment_user, container, false)
        val ivLogin = userview.findViewById<ImageView>(R.id.login)
        ivLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
        return userview
    }
}
