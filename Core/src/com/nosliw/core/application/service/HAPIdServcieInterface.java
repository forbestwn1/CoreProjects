package com.nosliw.core.application.service;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPIdServcieInterface extends HAPSerializableImp{

	private String m_id;
	
	public HAPIdServcieInterface(String id){
		this.m_id = id;
	}
	
	public String getId(){  return this.m_id;   }
	
	@Override
	protected String buildLiterate(){
		return this.m_id;
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.m_id = literateValue;
		return true;
	}

}
