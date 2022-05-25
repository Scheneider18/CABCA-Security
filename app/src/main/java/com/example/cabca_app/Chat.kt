package com.example.cabca_app
//Data class de la información que almacenara del chat
data class Chat(var image: Int = 0,
                var id: String = "",
                var name: String = "",
                var users: List<String> = emptyList())
