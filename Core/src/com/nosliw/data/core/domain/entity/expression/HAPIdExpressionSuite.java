package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPIdExpressionSuite extends HAPSerializableImp{

	private String m_id;
	
	public HAPIdExpressionSuite(String id){
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
