package com.mypay.remittance.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataRemittanceRequestRepository extends JpaRepository<RemittanceRequestJpaEntity, Long> {

    @Query("SELECT e FROM RemittanceRequestJpaEntity e WHERE e.fromMembershipId  = :membershipId")
    List<RemittanceRequestJpaEntity> findByMembershipId(@Param("membershipId") Long membershipId);
}
