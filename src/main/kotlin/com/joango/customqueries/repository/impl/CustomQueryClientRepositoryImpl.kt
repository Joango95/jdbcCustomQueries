package com.joango.customqueries.repository.impl

import com.joango.customqueries.model.Client
import com.joango.customqueries.repository.CustomQueryClientRepository
import com.joango.customqueries.utils.ClientRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.PreparedStatement

@Component
class CustomQueryClientRepositoryImpl(val jdbcTemplate: JdbcTemplate): CustomQueryClientRepository {

    override fun findUserByCustomQuery(customQueryMap: Map<String, Any>): List<Client> {
        val customQueryString = buildCustomQuery(
            "SELECT * FROM client",
            "AND",
            customQueryMap
        )

        return jdbcTemplate.query(
            { preparedStatementCreator(it.prepareStatement(customQueryString), customQueryMap) }
        ) { rs, num ->
            ClientRowMapper().mapRow(rs, num)
        }
    }

    private fun buildCustomQuery(
        baseQuery: String,
        joinOperator: String,
        queryMap: Map<String, Any>
    ): String {
        var customQuery = baseQuery
        if (queryMap.isEmpty()) return customQuery

        customQuery = customQuery.plus(" WHERE ")
        queryMap.entries.forEachIndexed { index, entry ->
            customQuery = when (entry.key) {
                "id" -> customQuery.plus("LOWER(id) LIKE '%' || LOWER(?) || '%'")
                "userName" -> customQuery.plus("LOWER(user_name) LIKE '%' || LOWER(?) || '%'")
                "phoneNumber" -> customQuery.plus("regexp_replace(" +
                        "CAST(phone_number AS TEXT), '[^0-9]', '', 'g') " +
                        "LIKE '%' || (regexp_replace(CAST(? AS TEXT), '[^0-9]', '', 'g')) || '%'")
                "updatedTime" -> customQuery.plus("EXTRACT(EPOCH FROM updated_time) > ?")
                "friends" -> customQuery.plus("friend_id_list @> (?::text[])")

                else -> when(entry.value) {
                    is String -> customQuery.plus("${entry.key} = ?")
                    is Number -> customQuery.plus("(${entry.key} AS NUMERIC) = ?")
                    is List<*> -> customQuery.plus("${entry.key} in ?")
                    else -> customQuery.plus("${entry.key} = ?")
                }
            }
            if (index != queryMap.size - 1) {
                customQuery = customQuery.plus(" $joinOperator ")
            }
        }
        return customQuery
    }

    private fun preparedStatementCreator(
        preparedStatement: PreparedStatement,
        customQueryMap: Map<String, Any>
    ): PreparedStatement {
        val preparedStatementFnc = { preparedStatement: PreparedStatement ->
            customQueryMap.values.forEachIndexed { index, value ->
                val valuePosition = index + 1
                when (value) {
                    is String -> preparedStatement.setString(valuePosition, value)
                    is Number -> preparedStatement.setLong(valuePosition, value.toLong())
                    is Boolean -> preparedStatement.setBoolean(valuePosition, value)
                    is List<*> -> preparedStatement.setArray(
                        valuePosition,
                        preparedStatement.connection.createArrayOf("TEXT", value.toTypedArray())
                    )
                }
            }
            preparedStatement
        }
        return preparedStatementFnc(preparedStatement)
    }
}