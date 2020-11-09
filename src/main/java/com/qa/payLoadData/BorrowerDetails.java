package com.qa.payLoadData;

public class BorrowerDetails {
	String firstname;
	String middleName;
	String lastName;
	String taxId;
	String dob;
	String city;
	String zipCode;
	String streetAddress;
	String state;
	public BorrowerDetails(String firstname, String middleName, String lastName, String taxId, String dob, String city,
			String zipCode, String streetAddress, String state) {
		super();
		this.firstname = firstname;
		this.middleName = middleName;
		this.lastName = lastName;
		this.taxId = taxId;
		this.dob = dob;
		this.city = city;
		this.zipCode = zipCode;
		this.streetAddress = streetAddress;
		this.state = state;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	

}
