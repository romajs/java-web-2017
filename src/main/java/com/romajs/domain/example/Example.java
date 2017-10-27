package com.romajs.domain.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Example {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Example() {
	}

	public Example(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
