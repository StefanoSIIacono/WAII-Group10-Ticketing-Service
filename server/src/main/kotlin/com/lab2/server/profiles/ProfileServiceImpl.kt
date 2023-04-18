package com.lab2.server.profiles

import com.lab2.server.exceptionsHandler.exceptions.DuplicateProfileException
import com.lab2.server.exceptionsHandler.exceptions.ProfileNotFoundException
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
        /*val dbProfile = profileRepository.findByIdOrNull(email)
            ?: throw ProfileNotFoundException("Profile doesn't exist!")

        dbProfile.setEmail(profile.email)
        dbProfile.setPassword(profile.password)*/
        if (!profileRepository.existsById(email))
            throw ProfileNotFoundException("Profile doesn't exist!")

        profileRepository.save(profile.toProfile())
    }
}