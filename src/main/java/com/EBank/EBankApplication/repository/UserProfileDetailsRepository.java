package com.EBank.EBankApplication.repository;

import com.EBank.EBankApplication.entity.UserProfileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileDetailsRepository extends JpaRepository<UserProfileDetails, Long> {
}
