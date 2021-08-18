package com.example.firestorechatapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestorechatapp.Utils.FirebaseUtils
import com.google.firebase.firestore.CollectionReference
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ChatFragment : Fragment(), View.OnClickListener {

    private lateinit var chatInput: EditText
    private lateinit var sendButton: FrameLayout
    private var chatAdapter: ChatAdapter? = null
    private lateinit var recyclerviewChat: RecyclerView
    private var messageList = arrayListOf<Message>()
    private var name: String? = null
    private var password: String? = null
    private val firebaseUtils: FirebaseUtils = FirebaseUtils()

    companion object {
        fun newInstance(): ChatFragment {
            val myFragment = ChatFragment()
            val args = Bundle()
            myFragment.arguments = args
            return myFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val mView: View  = LayoutInflater.from(activity).inflate(R.layout.chat_fragment, container, false)
        password = arguments?.getString("password")
        name= arguments?.getString("name")
        initViews(mView)

        return mView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViews(mView: View) {

        chatInput = mView.findViewById(R.id.chatInput)
        val chatIcon: ImageView = mView.findViewById(R.id.sendIcon)
        sendButton = mView.findViewById(R.id.sendButton)
        recyclerviewChat = mView.findViewById(R.id.chatRecyclerView)

        sendButton.isClickable = false
        sendButton.isEnabled = false

        val llm = LinearLayoutManager(activity)
        llm.reverseLayout = true
        recyclerviewChat.layoutManager = llm

        chatInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun afterTextChanged(s: Editable) {

                if (s.isNotEmpty()) {
                    chatIcon.setImageDrawable(activity?.getDrawable(R.drawable.ic_launcher_background))
                    sendButton.isClickable = true
                    sendButton.isEnabled = true
                }else {
                    chatIcon.setImageDrawable(activity?.getDrawable(R.drawable.ic_launcher_background))
                    sendButton.isClickable = false
                    sendButton.isEnabled = false
                }
            }
        })

        sendButton.setOnClickListener(this)
        chatAdapter = activity?.let { ChatAdapter(messageList.reversed(), it) }
        recyclerviewChat.adapter = chatAdapter

        //load firestore db
        val messages = getFirestoredb(firebaseUtils)
        //val messages = getFirestoredb(firebaseUtils)

        //continously listening for changes to db and updating layout herafter
        messages.addSnapshotListener { snapshot, e ->
            if (e != null) {
                FSOnSucessListener(messages)
                return@addSnapshotListener
            }
            FSOnSucessListener(messages)
        }
    }

    private fun getCurrentTime(): String {
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm.ss")
            .withZone(ZoneOffset.UTC)
            .format(Instant.now())
    }

    override fun onClick(p0: View?) {
        if (chatInput.text.isNotEmpty()){

            val msg = Message(chatInput.text.toString(), getCurrentTime(), Constants.MESSAGE_TYPE_SENT,
                name.toString())
            messageList.add(msg)
            updateUI()
            addMessageToFirebase(firebaseUtils, msg)
        }
    }

    private fun updateUI(){
        if (activity != null) {
            chatAdapter = ChatAdapter(messageList.reversed(), requireActivity())
            recyclerviewChat.adapter = chatAdapter
            chatInput.text.clear()
            recyclerviewChat.adapter?.notifyDataSetChanged() //TODO: Fix warning
            recyclerviewChat.scrollToPosition(0)
        }
    }

    private fun getFirestoredb(_firebaseUtils: FirebaseUtils): CollectionReference {

        //TODO: Kotlin coroutine?
        return _firebaseUtils.firestorePath
    }

    private fun addMessageToFirebase(_firebaseUtils: FirebaseUtils, msg: Message)
    {
        //TODO: Kotlin coroutine?
        _firebaseUtils.firestorePath.document(msg.time).set(msg)
    }

    //TODO: breaking single responsibility principle
    fun FSOnSucessListener(messages: CollectionReference)
    {
        //TODO: better if we could just fetch new changes instead of everything for each event
        messageList.clear()
        //TODO: Kotlin coroutine?
        messages.get().addOnSuccessListener { result ->

            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
                var tempType: Int
                if (name == document.data.get("name").toString()) {
                    tempType = Constants.MESSAGE_TYPE_SENT
                } else {
                    tempType = Constants.MESSAGE_TYPE_RECEIVED
                }
                messageList.add(
                    Message(
                        document.data["message"].toString(),
                        document.data["time"].toString(),
                        tempType,
                        document.data["name"].toString()
                    )
                )
            }
            updateUI()

        }

    }

}