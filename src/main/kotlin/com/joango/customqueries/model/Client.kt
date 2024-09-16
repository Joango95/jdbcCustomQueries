package com.joango.customqueries.model

import java.sql.Timestamp


data class Client(
    val id: String,
    val userName: String,
    val phoneNumber: Long,
    val friendIdList: List<String>,
    val updatedTime: Timestamp
)
