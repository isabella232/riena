package org.eclipse.riena.demo.common;

public class Contract {

	private String contractNo;
	private String description;
	private double contractValue;
	private String status;

	public Contract() {
		contractNo = ""; //$NON-NLS-1$
		description = ""; //$NON-NLS-1$
		status = ""; //$NON-NLS-1$

	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getContractValue() {
		return contractValue;
	}

	public void setContractValue(double contractValue) {
		this.contractValue = contractValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
