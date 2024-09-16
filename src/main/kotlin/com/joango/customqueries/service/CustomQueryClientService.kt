package com.joango.customqueries.service

import com.joango.customqueries.model.Client
import com.joango.customqueries.repository.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomQueryClientService(
    @Autowired
    private val userRepository: ClientRepository
) {

    fun getClientByCustomQuery(customQueryMap: Map<String, Any>): List<Client> {
        return userRepository.findUserByCustomQuery(customQueryMap)
    }
}