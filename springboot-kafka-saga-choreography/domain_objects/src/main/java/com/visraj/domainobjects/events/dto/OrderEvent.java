package com.visraj.domainobjects.events.dto;

import com.visraj.domainobjects.dto.CustomerOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderEvent {

	private CustomerOrder order;
	private String type;
	
}
