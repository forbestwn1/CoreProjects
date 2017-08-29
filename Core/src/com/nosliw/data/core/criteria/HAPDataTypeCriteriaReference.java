package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

/**
 * Criteria that reference to another criteria
 */
public class HAPDataTypeCriteriaReference extends HAPDataTypeCriteriaImp{

	private String m_reference;
	
	public HAPDataTypeCriteriaReference(String reference){
		this.m_reference = reference;
	}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_REFERENCE;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_REFERENCE));
		out.append(this.m_reference);
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_REFERENCE));
		return out.toString(); 
	}

	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return null;
	}

}
