package com.lab2.server.unitTest

import com.lab2.server.data.*
import com.lab2.server.dto.ExpertDTO
import com.lab2.server.dto.ExpertiseDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.exceptionsHandler.exceptions.ExpertNotFoundException
import com.lab2.server.repositories.ExpertRepository
import com.lab2.server.serviceImpl.ExpertServiceImpl
import com.lab2.server.services.ExpertiseService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class ExpertServiceTest {
    private val repository = mockk<ExpertRepository>()
    private val expertiseService = mockk<ExpertiseService>()

    @Test
    fun getAllTest() {
        val expertList = mutableListOf(
            Expert("e1@e1.com","John", "Doe"),
            Expert("e2@e2.com", "Jane", "Smith")
        )
        every { repository.findAll() } returns expertList

        val service = ExpertServiceImpl(repository, expertiseService)
        // when
        val result = service.getAll()

        // then
        verify(exactly = 1) { repository.findAll() }
        assertEquals(expertList.map { it.toDTO() }.toMutableSet(), result)
    }

    @Test
    fun getExpertByEmailTest() {
        // given
        val expert = Expert("e1@e1.com", "John", "Doe")
        every { repository.findByIdOrNull(expert.email) } returns expert

        val service = ExpertServiceImpl(repository, expertiseService)
        // when
        val result = service.getExpertByEmail(expert.email)

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expert.email) }
        assertEquals(expert.toDTO(), result)
    }

    @Test
    fun getExpertByEmailNotFoundTest() {
        // given
        val expertEmail = "e1@e1.com"
        every { repository.findByIdOrNull(expertEmail) } returns null

        val service = ExpertServiceImpl(repository, expertiseService)
        // when/then
        try {
            service.getExpertByEmail(expertEmail)
        } catch (e: ExpertNotFoundException) {
            assertEquals("Expert not found", e.message)
        }

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expertEmail) }
    }

    @Test
    fun getExpertisesByExpertTest() {
        // given
        val expert = Expert("e1@e1.com", "John", "Doe")
        val expertiseList = mutableListOf(
            Expertise("Backend"),
            Expertise("Frontend")
        )
        expert.expertises.addAll(expertiseList)
        every { repository.findByIdOrNull(expert.email) } returns expert

        val service = ExpertServiceImpl(repository, expertiseService)
        // when
        val result = service.getExpertisesByExpert(expert.email)

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expert.email) }
        assertEquals(expertiseList.map { it.toDTO() }.toMutableSet(), result)
    }

    @Test
    fun getExpertisesByExpertNotFoundTest() {
        // given
        val expertEmail = "e1@e1.com"
        every { repository.findByIdOrNull(expertEmail) } returns null

        val service = ExpertServiceImpl(repository, expertiseService)
        // when/then
        try {
            service.getExpertisesByExpert(expertEmail)
        } catch (e: ExpertNotFoundException) {
            assertEquals("Expert not found", e.message)
        }

        // then
        verify(exactly = 1) { repository.findByIdOrNull(expertEmail) }
    }

    @Test
    fun insertExpertTest() {
        // given
        val expertEmail = "e1@e1.com"
        val expertName = "John"
        val expertSurname = "Doe"
        val expertise = "COMPUTER"
        val expertiseDTO = ExpertiseDTO(1, expertise)
        val expert = Expert(expertEmail, expertName, expertSurname)
        val expertDTO = ExpertDTO(expertEmail, expertName, expertSurname)
        every { repository.save(any()) } returns expert
        every { repository.findByIdOrNull(expertDTO.email) } returns expert
        every { expertiseService.getExpertise(expertise) } returns expertiseDTO

        val service = ExpertServiceImpl(repository, expertiseService)
        // when
        service.insertExpert(expertDTO, mutableSetOf(expertise))

        // then
        verify(exactly = 2) { repository.save(any()) }
        verify(exactly = 1) { repository.findByIdOrNull(expertDTO.email)}
        verify(exactly = 1) { expertiseService.getExpertise(expertise) }
    }


    @Test
    fun addExpertiseToExpertTest() {
        // given
        val expert = ExpertDTO("e1@e1.com", "John", "Doe")
        val expertise = ExpertiseDTO(1L, "Backend")

        every { repository.findByIdOrNull(expert.email) } returns expert.toExpert()
        every { repository.save(any()) } returns expert.toExpert()
        every { expertiseService.getExpertise(expertise.field) } returns expertise

        val service = ExpertServiceImpl(repository, expertiseService)
        // when
        service.addExpertiseToExpert(expert.email, expertise.field)
        // then
        verify(exactly = 1) { repository.findByIdOrNull(expert.email) }
        verify(exactly = 1) { expertiseService.getExpertise(expertise.field) }
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun addTicketToExpertTest() {
        // given
        val expert = Expert(
            "e1@e1.com",
            "John",
            "Doe"
        )
        val product = Product(
            "1",
            "Shirt",
            "Tommy Hilfiger"
        )
        val profile = Profile(
            "test@test1.com",
            "testName",
            "testSurname",
            null
        )
        val address = Address(
            "c",
            "c",
            "z",
            "s",
            "h",
            profile
        )
        val expertise= Expertise( "COMPUTER")
        profile.addAddress(address)
        val ticket = Ticket(
            "Ticket",
            expertise,
            Priority.TOASSIGN,
            profile,
            null,
            product
        )

        every { repository.save(any()) } returns expert

        val service = ExpertServiceImpl(repository, expertiseService)
        // when
        service.addTicketToExpert(expert, ticket)

        // then
        assertEquals(1, expert.inProgressTickets.size)
        assertTrue(expert.inProgressTickets.contains(ticket))
        verify(exactly = 1) { repository.save(expert) }
    }
}