package com.joango.customqueries.repository

import com.joango.customqueries.model.Client
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository: CrudRepository<Client, Int>, CustomQueryClientRepository