package com.nosliw.common.exception;

/*
 * exception as a wraper of service data
 * all the information about the error is within service data
 */
public class HAPServiceDataException extends Exception{

	private static final long serialVersionUID = -8946531860668888707L;
	
	private HAPServiceData m_serviceData;
	
	public HAPServiceDataException(HAPServiceData data){
		this.m_serviceData = data;
	}
	
	public HAPServiceData getServiceData(){
		return this.m_serviceData;
	}
	
}
