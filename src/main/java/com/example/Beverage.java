package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Beverage {

	@Id
	@GeneratedValue
	private Long id;

	private String beverageName;
	private double alcoholContent;

	public Beverage() { // JPA..
	}

	public Beverage(String name, double alc) {
		beverageName = name;
		alcoholContent = alc;
	}

	public Long getId() {
		return id;
	}

	public String getBeverageName() {
		return beverageName;
	}

	public void setBeverageName(String beverageName) {
		this.beverageName = beverageName;
	}

	public double getAlcoholContent() {
		return alcoholContent;
	}

	public void setAlcoholContent(double alcoholContent) {
		this.alcoholContent = alcoholContent;
	}
}