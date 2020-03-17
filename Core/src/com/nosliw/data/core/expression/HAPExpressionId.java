package com.nosliw.data.core.expression;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPExpressionId extends HAPSerializableImp{

	private String m_expressionId;
	private String m_suiteId;
	
	public HAPExpressionId(String suiteId, String expressionId){
		this.m_expressionId = expressionId;
		this.m_suiteId = suiteId;
	}

	public HAPExpressionId(String id){
		this.parseId(id);
	}

	public String getId(){  return HAPNamingConversionUtility.cascadeLevel1(m_expressionId, m_suiteId);  }

	public String getSuiteId() {   return this.m_suiteId;   }
	public String getExpressionId() {    return this.m_expressionId;    }
	
	private void parseId(String id) {
		String[] segs = HAPNamingConversionUtility.parseLevel1(id);
		this.m_expressionId = segs[0];
		if(segs.length>1)	this.m_suiteId = segs[1];
	}
	
	@Override
	protected String buildLiterate(){		return this.getId();	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.parseId(literateValue);
		return true;
	}
}
