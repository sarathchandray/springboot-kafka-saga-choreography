package com.visraj.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Payment {

	@Id
	@GeneratedValue
	private Long id;
	private Long mode;
	private Long orderId;
	private double amount;
	private String status;
}
