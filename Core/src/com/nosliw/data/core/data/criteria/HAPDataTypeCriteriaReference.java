package com.nosliw.data.core.data.criteria;

import com.nosliw.common.utils.HAPConstantShared;

/**
 * Criteria that reference to another criteria
 */
public class HAPDataTypeCriteriaReference extends HAPDataTypeCriteriaAbstract{

	private String m_reference;
	
	public HAPDataTypeCriteriaReference(String reference){
		this.m_reference = reference;
	}
	
	@Override
	public String getType() {		return HAPConstantShared.DATATYPECRITERIA_TYPE_REFERENCE;	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_REFERENCE));
		out.append(this.m_reference);
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_REFERENCE));
		return out.toString(); 
	}

}
