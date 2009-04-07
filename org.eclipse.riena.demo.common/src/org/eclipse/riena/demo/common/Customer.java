package org.eclipse.riena.demo.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {

	private String custno;
	private String lastName;
	private String firstName;
	private Address address;
	private Date birthDate;
	private float salary;
	private List<Contract> contracts;

	public Customer() {
		address = new Address();
		contracts = new ArrayList<Contract>();
		lastName = ""; //$NON-NLS-1$
		firstName = ""; //$NON-NLS-1$
		custno = ""; //$NON-NLS-1$
		birthDate = null;
		salary = 0;
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

}
