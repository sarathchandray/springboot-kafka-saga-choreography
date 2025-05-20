package com.visraj.domainobjects.events.dto;

import com.visraj.domainobjects.dto.CustomerOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEvent {

	private CustomerOrder order;
	private String type;
	
}
