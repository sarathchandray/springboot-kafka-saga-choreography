package com.visraj.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Order {

	@Id
	@GeneratedValue
	private long id;
	private String item;
	private int quantity;
	private double amount;
	private String status;
}
