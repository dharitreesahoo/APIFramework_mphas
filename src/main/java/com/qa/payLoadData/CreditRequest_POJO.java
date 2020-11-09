package com.qa.payLoadData;

public class CreditRequest_POJO {
	String creditReportRequestAction;
	String requestPartyName;
	BorrowerDetails borrowerDetails;
	
	
	public CreditRequest_POJO(String creditReportRequestAction, String requestPartyName,
			BorrowerDetails borrowerDetails) {
		super();
		this.creditReportRequestAction = creditReportRequestAction;
		this.requestPartyName = requestPartyName;
		this.borrowerDetails = borrowerDetails;
	}
	public String getCreditReportRequestAction() {
		return creditReportRequestAction;
	}
	public void setCreditReportRequestAction(String creditReportRequestAction) {
		this.creditReportRequestAction = creditReportRequestAction;
	}
	public String getRequestPartyName() {
		return requestPartyName;
	}
	public void setRequestPartyName(String requestPartyName) {
		this.requestPartyName = requestPartyName;
	}
	public BorrowerDetails getBorrowerDetails() {
		return borrowerDetails;
	}
	public void setBorrowerDetails(BorrowerDetails borrowerDetails) {
		this.borrowerDetails = borrowerDetails;
	}
	
	

}
