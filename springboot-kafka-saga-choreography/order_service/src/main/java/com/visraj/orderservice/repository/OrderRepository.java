package com.visraj.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visraj.orderservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
