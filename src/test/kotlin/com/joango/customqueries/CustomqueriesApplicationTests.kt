package com.joango.customqueries

import com.joango.customqueries.service.CustomQueryClientService
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.junitpioneer.jupiter.DisableIfTestFails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@RunWith(SpringRunner::class)
@ExtendWith(SpringExtension::class)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DisableIfTestFails
class CustomQueriesApplicationTests(
) {

	companion object {
		private const val POSTGRES_PORT = 5432
		private const val POSTGRES_USER = "postgres"
		private const val POSTGRES_PASSWORD = "postgres"
		private const val POSTGRES_DB = "customQueriesDemo"
	}

	@Container
	private val postgresTestContainer = PostgreSQLContainer("postgres:9.6.12")
		.withInitScript("schema.sql")
		.withExposedPorts(POSTGRES_PORT)
		.withUsername(POSTGRES_USER)
		.withPassword(POSTGRES_PASSWORD)
		.withDatabaseName(POSTGRES_DB)

	@Autowired
	lateinit var customQueryClientService: CustomQueryClientService


	init {
		postgresTestContainer.portBindings = listOf("${POSTGRES_PORT}:${POSTGRES_PORT}")
		postgresTestContainer.start()
	}

	@Test
	fun contextLoads() {}

	@Test
	fun `custom query client service should return users matching name and phone`() {
		val nameToSearch = "Sam"
		val phoneNumberToSearch = 321

		val queryByNameAndPhoneNumber = customQueryClientService.getClientByCustomQuery(
			mapOf(
				"userName" to nameToSearch,
				"phoneNumber" to phoneNumberToSearch
			)
		)

		assertEquals(queryByNameAndPhoneNumber.size, 2)
		assertTrue {
			queryByNameAndPhoneNumber.all { it.userName.contains(nameToSearch) }
					&& queryByNameAndPhoneNumber.all { it.phoneNumber.toString().contains(phoneNumberToSearch.toString()) }
		}
	}

	@Test
	fun `custom query client service should return users matching name and firendIds`() {
		val nameToSearch = "Joe"
		val friendIdListToSearch = listOf("6", "8")

		val queryByNameAndEmail = customQueryClientService.getClientByCustomQuery(
			mapOf(
				"userName" to nameToSearch,
			)
		)

		assertEquals(queryByNameAndEmail.size, 1)
		assertTrue {
			queryByNameAndEmail.all { it.userName.contains(nameToSearch) }
					&& queryByNameAndEmail.all { it.friendIdList.containsAll(friendIdListToSearch) }
		}
	}

	@Test
	fun `custom query client service should return users matching updatedTime`() {
		val updatedTimeToSearch = 1798761600L
		val queryByUpdatedTime = customQueryClientService.getClientByCustomQuery(
			mapOf(
				"updatedTime" to updatedTimeToSearch
			)
		)

		assertEquals(queryByUpdatedTime.size, 2)
		assertTrue {
			queryByUpdatedTime.all { it.updatedTime.time > updatedTimeToSearch }
		}
	}

	@Test
	fun `custom query client service should return users matching friends`() {
		val firstFriendListToSearch = listOf("8")
		val secondFriendListToSearch = listOf("4", "5")

		val queryByFriendListOne = customQueryClientService.getClientByCustomQuery(
			mapOf(
				"friends" to listOf(8)
			)
		)

		val queryByFriendListTwo = customQueryClientService.getClientByCustomQuery(
			mapOf(
				"friends" to listOf(4, 5)
			)
		)

		assertEquals(queryByFriendListOne.size, 4)
		assertTrue {
			queryByFriendListOne.all { it.friendIdList.containsAll(firstFriendListToSearch) }
		}

		assertEquals(queryByFriendListTwo.size, 2)
		assertTrue {
			queryByFriendListTwo.all { it.friendIdList.containsAll(secondFriendListToSearch) }
		}
	}

}
