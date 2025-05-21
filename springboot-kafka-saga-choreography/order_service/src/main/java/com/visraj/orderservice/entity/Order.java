package com.visraj.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue
	private long id;
	private String item;
	private int quantity;
	private double amount;
	private String status;
}
