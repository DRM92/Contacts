package com.example.contacts;

/* Creating a structure - Can create contact objects rather than string objects */

public class Contact {
	private String name;
	private String address;
	private String email;
	private String number;
	private String age;
	private int pkey;
	
	public Contact() {
		
	}
	
	public Contact(int i, String name, String address, String email, String number, String age){
		this.pkey=i;
		this.name=name;
		this.address=address;
		this.email=email;
		this.number=number;
		this.age=age;
	}
	
	/* Get and set methods to be used in database handler */
	
	public void setPkey(int pkey){
		this.pkey=pkey;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setAddress(String address){
		this.address=address;
	}
	
	public void setEmail(String email){
		this.email=email;
	}
	
	public void setNumber(String number){
		this.number=number;
	}
	
	public void setAge(String age){
		this.age=age;
	}
	
	public int getpKey(){
		return pkey;
	}
	
	public String getName(){
		return name;
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getNumber(){
		return number;
	}
	
	public String getAge(){
		return age;
	}
	
	@Override
	public String toString() {
		return "contact [key = " + name + ", address = " + address + " "
				+ "email = " + email + "number = " + number + age + "]";		
	}	
}