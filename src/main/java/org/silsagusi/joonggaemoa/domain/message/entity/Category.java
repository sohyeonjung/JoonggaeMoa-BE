package org.silsagusi.joonggaemoa.domain.message.entity;

public enum Category {
	BIRTHDAY("birthday"),
	EXPIRATION("expiration"),
	WELCOME("welcome"),
	BULK("bulk"),
	;

	private final String label;

	Category(String label) {
		this.label = label;
	}
}
