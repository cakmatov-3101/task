package com.test.task.repository;

import com.test.task.entity.Balance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends CrudRepository<Balance, Integer> {

    @Query("select b from Balance b where b.clientId = :clientId")
    public Balance findByClientId(@Param("clientId") final Integer clientId);
}
