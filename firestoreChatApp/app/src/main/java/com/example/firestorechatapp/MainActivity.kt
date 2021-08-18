package com.example.firestorechatapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firestorechatapp.ViewModels.userViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity()/*, ChatFragment.CommunicationListener*/
{
    private lateinit var chatFragment: ChatFragment
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    // private var userViewModel: userViewModel?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var password_edit: EditText
    private lateinit var name_edit: EditText
    private var password: String = ""
    private var name: String = ""
    private var keyboardIsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //create all listeners
        createListeners()
    }

    public override fun onStart() {
        super.onStart()
        // TODO: There should be a case for checking if user is already signed in
        //currentUser = auth.currentUser
        /*
        if(currentUser != null){
        }
         */
    }

    private fun createListeners()
    {
        password_edit = findViewById(R.id.edit_password)
        name_edit = findViewById(R.id.edit_name)
        loginButton = findViewById(R.id.button_login)
        registerButton = findViewById(R.id.button_register)

        password_edit.setOnClickListener()
        {
            if(!keyboardIsShown)
            {
                showSoftKeyboard()
                keyboardIsShown = true
            }
        }

        name_edit.setOnClickListener()
        {
            if(!keyboardIsShown) {
                showSoftKeyboard()
                keyboardIsShown = true
            }
        }

        loginButton.setOnClickListener()
        {
            password = password_edit.text.toString()
            name = name_edit.text.toString()
            signInWithFSUser(name, password, it)
        }

        registerButton.setOnClickListener()
        {
            password = password_edit.text.toString()
            name = name_edit.text.toString()
            createNewFSUser(name, password)
        }
    }

    //TODO: Firebase error handling + show user
    //TODO: coroutine?
    private fun signInWithFSUser(_username: String, _password: String, it: View) {

        if(_username != "" && _password != "") {

            auth.signInWithEmailAndPassword(_username, _password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")

                        //TODO: Below calls are technically breaking single responsibility principle..
                        closeSoftKeyboard(it)
                        keyboardIsShown = false
                        showChatFragment(bundleUserInfo())

                        //val user = auth.currentUser //TODO: currentUser can be used to getting FS info from

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else
        {
            Toast.makeText(
                baseContext, "You must provide password and email.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //TODO: Firebase error handling + show user
    //TODO: coroutine?

    private fun createNewFSUser(_username: String, _password: String)
    {
        if(_username != "" && _password != "") {
            auth.createUserWithEmailAndPassword(_username, _password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        name = _username
                        password = _password
                        Log.d(TAG, "createUserWithEmail:success")

                        Toast.makeText(
                            baseContext, "User created.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                        //TODO: No case for if user is already created
                        Toast.makeText(
                            baseContext, "Failed creating user. Password must be 6 characters long \n" +
                                    "and email of the format xxxx@xxx.com or .dk or .qer",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else
        {
            Toast.makeText(
                baseContext, "You must provide password and email.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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

    /*TODO: These are meant to close and show keyboard when pressing login,
       and showing chat fragment, but there is a better way of doing this.
       Doing it this way seems to cause small layout bugs */
    private fun closeSoftKeyboard(v: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun showSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun bundleUserInfo(): Bundle
    {
        val bundle = Bundle()
        bundle.putString("password", password)
        bundle.putString("name", name)
        return bundle
    }

}