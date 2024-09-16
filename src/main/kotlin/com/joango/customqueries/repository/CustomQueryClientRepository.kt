package com.joango.customqueries.repository

import com.joango.customqueries.model.Client

interface CustomQueryClientRepository {
    fun findUserByCustomQuery(customQueryMap: Map<String, Any>): List<Client>
}