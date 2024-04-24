package com.nosliw.core.application.brick.adapter.dataassociation;

import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPDataAssociation extends HAPExecutableImp{

	private String m_dataAssociationType;
	
	public HAPDataAssociation(String dataAssociationType) {
		this.m_dataAssociationType = dataAssociationType;
	}
	
	public String getDataAssociationType() {   return this.m_dataAssociationType;     }
	
}
