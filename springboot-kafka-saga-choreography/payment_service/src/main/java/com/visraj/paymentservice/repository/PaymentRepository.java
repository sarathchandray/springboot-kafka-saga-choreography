package com.visraj.paymentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visraj.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	//public List<Payment> findByOrderId(long orderId);
}