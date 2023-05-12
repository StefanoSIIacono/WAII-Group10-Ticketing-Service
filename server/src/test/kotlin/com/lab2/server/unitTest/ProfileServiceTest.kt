package com.lab2.server.unitTest

import com.lab2.server.dto.toDTO
import com.lab2.server.data.Profile
import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.serviceImpl.ProfileServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class ProfileServiceTest {
    private val repository = mockk<ProfileRepository>()

    @Test
    fun getAllTest() {
        // given
        val profileList = mutableListOf(
            Profile("mariorossi@gmail.com", "Mario", "Rossi"),
            Profile("luigiverdi@gmail.com", "Luigi", "Verdi")
        )
        every { repository.findAll() } returns profileList

        val service = ProfileServiceImpl(repository)
        // when
        val result = service.getAll()

        // then
        verify(exactly = 1) { repository.findAll() }
        assertEquals(profileList.map { it.toDTO() }, result)
    }

    @Test
    fun getProfileByEmailTest() {
        // given
        val profile = Profile("mariorossi@gmail.com", "mario", "ross")
        every { repository.findByIdOrNull("mariorossi@gmail.com") } returns profile

        val service = ProfileServiceImpl(repository)
        // when
        val result = service.getProfileByEmail("mariorossi@gmail.com")

        // then
        verify(exactly = 1) { repository.findByIdOrNull("mariorossi@gmail.com") }
        assertEquals(profile.toDTO(), result)
    }

    @Test
    fun getNotExistentProfileByEmailTest() {
        // given
        every { repository.findByIdOrNull("mariorossi@gmail.com") } returns null

        val service = ProfileServiceImpl(repository)
        // when
        val result = service.getProfileByEmail("mariorossi@gmail.com")

        // then
        verify(exactly = 1) { repository.findByIdOrNull("mariorossi@gmail.com") }
        assertEquals(null, result)
    }

    @Test
    fun insertProfileTest() {
        // given
        val profile = Profile("mariorossi@gmail.com", "mario", "ross")

        every { repository.existsById("mariorossi@gmail.com") } returns false
        every { repository.save(any())  } returns profile

        val service = ProfileServiceImpl(repository)
        // when
        service.insertProfile(profile.toDTO())

        // then
        verify(exactly = 1) { repository.existsById("mariorossi@gmail.com")  }
        verify(exactly = 1) { repository.save(any())  }
    }

    @Test
    fun insertExistingProfileTest() {
        // given
        val profile = Profile("mariorossi@gmail.com", "mario", "ross")

        every { repository.existsById("mariorossi@gmail.com") } returns true
        every { repository.save(any())  } returns profile

        val service = ProfileServiceImpl(repository)
        // when
        var exceptionThrown = false
        try {
            service.insertProfile(profile.toDTO())
        } catch (e: DuplicateProfileException) {
            exceptionThrown = true
        }

        // then

        verify(exactly = 1) { repository.existsById("mariorossi@gmail.com")  }
        verify(exactly = 0) { repository.save(any())  }
        assertEquals(exceptionThrown, true)
    }

    @Test
    fun editProfileTest() {
        // given
        val profile = Profile("mariorossi@gmail.com", "mario", "rossi")

        every { repository.existsById("mariorossi@gmail.com") } returns true
        every { repository.save(any())  } returns profile

        val service = ProfileServiceImpl(repository)
        // when
        service.editProfile("mariorossi@gmail.com", profile.toDTO())

        // then
        verify(exactly = 1) { repository.existsById("mariorossi@gmail.com")  }
        verify(exactly = 1) { repository.save(any())  }
    }

    @Test
    fun editNotExistingProfileTest() {
        // given
        val profile = Profile("mariorossi@gmail.com", "mario", "rossi")

        every { repository.existsById("mariorossi@gmail.com") } returns false
        every { repository.save(any())  } returns profile

        val service = ProfileServiceImpl(repository)
        // when
        var exceptionThrown = false
        try {
            service.editProfile("mariorossi@gmail.com", profile.toDTO())
        } catch (e: ProfileNotFoundException) {
            exceptionThrown = true
        }

        // then

        verify(exactly = 1) { repository.existsById("mariorossi@gmail.com")  }
        verify(exactly = 0) { repository.save(any())  }
        assertEquals(exceptionThrown, true)
    }

}