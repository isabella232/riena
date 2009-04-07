package org.eclipse.riena.demo.common;

public class Address {
	private String zipCode;
	private String city;
	private String street;

	public Address() {
		zipCode = city = street = ""; //$NON-NLS-1$
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

}
