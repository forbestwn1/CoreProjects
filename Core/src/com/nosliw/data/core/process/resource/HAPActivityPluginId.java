package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPActivityPluginId extends HAPSerializableImp{

	private String m_id;
	
	public HAPActivityPluginId(String id){
		this.m_id = id;
	}
	
	public String getId(){  return this.m_id;  }

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
