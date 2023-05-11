package com.lab2.server.serviceImpl

import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.data.toProfile
import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.ProfileEmailChangeNotAllowedException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
import com.lab2.server.repositories.ProfileRepository
import com.lab2.server.services.ProfileService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository): ProfileService {
    override fun getAll(): List<ProfileDTO> {
        return profileRepository.findAll().map { it.toDTO() }
    }

    override fun getProfileByEmail(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)?.toDTO()
    }
    override fun insertProfile(profile: ProfileDTO){
        if (profileRepository.existsById(profile.email))
            throw DuplicateProfileException("Profile exists!")

        profileRepository.save(profile.toProfile())
    }

    override fun editProfile(email: String, profile: ProfileDTO){
        if (email != profile.email)
            throw ProfileEmailChangeNotAllowedException("Can't change profile email")
        if (!profileRepository.existsById(email))
            throw ProfileNotFoundException("Profile doesn't exist!")

        profileRepository.save(profile.toProfile())
    }
}