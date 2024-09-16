package com.joango.customqueries.utils

import com.joango.customqueries.model.Client
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ClientRowMapper: RowMapper<Client> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Client {
        return Client(
            id = rs.getString("id"),
            userName = rs.getString("user_name"),
            phoneNumber = rs.getLong("phone_number"),
            friendIdList = (rs.getArray("friend_id_list").array as Array<String>).toList(),
            updatedTime = rs.getTimestamp("updated_time")
        )
    }
}