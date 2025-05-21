package com.visraj.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="payments")
public class Payment {

	@Id
	@GeneratedValue
	private Long id;
	private String mode;
	private Long orderId;
	private double amount;
	private String status;
}
