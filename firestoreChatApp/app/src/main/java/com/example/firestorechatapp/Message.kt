package com.example.firestorechatapp

/**
 * Created by ramankit on 25/7/17.
 */
data class Message(var message: String, var time: String, var type: Int, var name: String)
/*TODO: Should not be necessary to include 'type' in class: functionality of adapter
    should be able to work solely on name
 */