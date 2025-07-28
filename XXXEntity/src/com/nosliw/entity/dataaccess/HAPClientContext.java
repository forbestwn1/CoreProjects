package com.nosliw.entity.dataaccess;

public class HAPClientContext{

	private HAPClientContextInfo m_clientContextInfo;
	
	private HAPDataContext m_dataContext;

	public HAPClientContext(HAPClientContextInfo clientContextInfo, HAPDataContext dataContext){
		this.m_clientContextInfo = clientContextInfo;
		this.m_dataContext = dataContext;
	}
	
	public HAPClientContextInfo getClientContextInfo(){ return this.m_clientContextInfo; }
	public HAPDataContext getDataContext(){ return this.m_dataContext; }

}
