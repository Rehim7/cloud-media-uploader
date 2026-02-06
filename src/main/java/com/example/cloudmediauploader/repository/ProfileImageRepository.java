package com.example.cloudmediauploader.repository;

import com.example.cloudmediauploader.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
