package com.devsuperior.dscommerce.repositories;

import com.devsuperior.dscommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {

    @NonNull
    @Query("SELECT DISTINCT obj FROM Order obj " +
            "JOIN FETCH obj.client " +
            "LEFT JOIN FETCH obj.payment "+
            "JOIN FETCH obj.items item " +
            "JOIN FETCH item.id.product " +
            "WHERE obj.id = :id")
    Optional<Order> findById(@NonNull Long id);
}
