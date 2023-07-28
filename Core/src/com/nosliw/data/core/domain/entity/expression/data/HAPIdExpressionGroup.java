package com.nosliw.data.core.domain.entity.expression.data;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPIdExpressionGroup extends HAPSerializableImp{

	private String m_expressionGroupId;
	private String m_suiteId;
	
	public HAPIdExpressionGroup(String suiteId, String expressionGroupId){
		this.m_expressionGroupId = expressionGroupId;
		this.m_suiteId = suiteId;
	}

	public HAPIdExpressionGroup(String id){
		this.parseId(id);
	}

	public String getId(){  return HAPUtilityNamingConversion.cascadeLevel1(m_expressionGroupId, m_suiteId);  }

	public String getSuiteId() {   return this.m_suiteId;   }
	public String getExpressionGroupId() {    return this.m_expressionGroupId;    }
	
	private void parseId(String id) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(id);
		this.m_expressionGroupId = segs[0];
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
