package com.example.firestorechatapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.firestorechatapp.ViewModels.userViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity()/*, ChatFragment.CommunicationListener*/
{
    private lateinit var chatFragment: ChatFragment
    private lateinit var chatButton: Button
    private var userViewModel: userViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val password_edit: EditText = findViewById(R.id.edit_password)
        val name_edit: EditText = findViewById(R.id.edit_name)

        password_edit.setOnClickListener()
        {
            showSoftKeyboard(this.applicationContext, it)
        }

        name_edit.setOnClickListener()
        {
            showSoftKeyboard(applicationContext, it)
        }

        chatButton = findViewById(R.id.button_login)
        chatButton.setOnClickListener()
        {
            //TODO: LiveData instead?

            val password = password_edit.text
            val name = name_edit.text

            val bundle = Bundle()
            bundle.putString("password", password.toString())
            bundle.putString("name", name.toString())

            name_edit.inputType = InputType.TYPE_NULL
            password_edit.inputType = InputType.TYPE_NULL
            closeSoftKeyboard(applicationContext, it)
            showChatFragment(bundle)
        }

    }

    /*TODO: there is a better way of doing this. This hides the blinking thing when going
        when going back and pressing the edit fields again
     */
    private fun closeSoftKeyboard(context: Context, v: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun showSoftKeyboard(context: Context, v: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

/*
    override fun onCommunication(message: String) {
        sendMessage(message)
    }

    private fun sendMessage(message: String) {
    }
*/
    private fun showChatFragment(bundle: Bundle) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        chatFragment = ChatFragment.newInstance()
        chatFragment.arguments = bundle
        //chatFragment.setCommunicationListener(this)
        fragmentTransaction.replace(R.id.mainScreen, chatFragment, "ChatFragment")
        fragmentTransaction.addToBackStack("ChatFragment")
        fragmentTransaction.commit()
    }

}