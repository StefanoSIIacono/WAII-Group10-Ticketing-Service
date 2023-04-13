package com.lab2.server.profiles

import com.lab2.server.products.toDTO
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
        profileRepository.save(profile.toProfile())
    }

    override fun editProfile(email: String, profile: ProfileDTO){
        profileRepository.deleteById(email)
        profileRepository.save(profile.toProfile())
    }
}