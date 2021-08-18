package com.example.firestorechatapp.Utils

import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtils {
    var fireStoreDatabase = FirebaseFirestore.getInstance()
    var firestorePath =  fireStoreDatabase.collection("chat")
        .document("channels").collection("channel1").document("data")
        .collection("messages")
    /*
    TODO: When app is updated to be not just one channel, firestore path must be changed so users
      read only from their own private channels
    */

}