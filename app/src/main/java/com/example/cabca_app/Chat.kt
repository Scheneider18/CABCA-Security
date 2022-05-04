package com.example.cabca_app

data class Chat(var image: Int = 0,
                var id: String = "",
                var name: String = "",
                var users: List<String> = emptyList())
