package com.example.firestorechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.firestorechatapp.ViewModels.userViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), ChatFragment.CommunicationListener {
    private lateinit var chatFragment: ChatFragment
    private lateinit var chatButton: Button
    private var userViewModel: userViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatButton = findViewById(R.id.button_login)
        chatButton.setOnClickListener()
        {
            val password = findViewById<EditText>(R.id.edit_password).text
            val name = findViewById<EditText>(R.id.edit_name).text

            val bundle = Bundle()
            bundle.putString("password", password.toString())
            bundle.putString("name", name.toString())
            showChatFragment(bundle)
        }
    }

    override fun onCommunication(message: String) {
        sendMessage(message)
    }

    private fun sendMessage(message: String) {
    }

    private fun showChatFragment(bundle: Bundle) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        chatFragment = ChatFragment.newInstance()
        chatFragment.arguments = bundle
        chatFragment.setCommunicationListener(this)
        fragmentTransaction.replace(R.id.mainScreen, chatFragment, "ChatFragment")
        fragmentTransaction.addToBackStack("ChatFragment")
        fragmentTransaction.commit()
    }

}