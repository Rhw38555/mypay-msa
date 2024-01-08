package com.mypay.banking.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataFirmbankingRequestRepository extends JpaRepository<RequestFirmbankingJpaEntity, Long> {
    @Query("SELECT e FROM RequestFirmbankingJpaEntity e WHERE e.aggregateIdentifier = :aggregateIdentifier")
    List<RequestFirmbankingJpaEntity> findByAggregateIdentifer(@Param("aggregateIdentifier") String aggregateIdentifier);
}
