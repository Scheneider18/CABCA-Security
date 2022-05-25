package com.example.cabca_app
import java.util.*
//Data class de la información que almacenara cada uno de los mensajes del chat
data class Message(var message: String = "",
                   var from: String = "",
                   var dob: Date = Date()
)
