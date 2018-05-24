package com.vlad.models;

public class User {
	
	private String id;
	private String name;
	private String lastName;
	private int age;

	public User(String id, String name, String lastName, int age) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.age = age;
	}
	
	public User() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", lastName=" + lastName + ", age=" + age + "]";
	}
	
}
