package com.visraj.domainobjects.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrder {
	private long orderId;

	private String item;

	private int quantity;

	private double amount;

	private String paymentMode;

	private String address;

}