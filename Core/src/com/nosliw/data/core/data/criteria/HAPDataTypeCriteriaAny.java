package com.nosliw.data.core.data.criteria;

import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeId;

public class HAPDataTypeCriteriaAny extends HAPDataTypeCriteriaImp{

	private static HAPDataTypeCriteriaAny m_instance;
	
	private HAPDataTypeCriteriaAny(){}
	
	public static HAPDataTypeCriteriaAny getCriteria(){
		if(m_instance==null){
			m_instance = new HAPDataTypeCriteriaAny();
		}
		return m_instance;
	}
	
	@Override
	public String getType() {		return HAPConstantShared.DATATYPECRITERIA_TYPE_ANY;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {	return true; }

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {		return true;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {		
		throw new IllegalStateException();	
	}
	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {
		throw new IllegalStateException();	
	}

//	@Override
//	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {		return this;	}

	@Override
	protected String buildLiterate(){		return HAPParserCriteria.getInstance().getToken(HAPParserCriteria.ANY);	}

	@Override
	public boolean equals(Object obj){
		return obj instanceof HAPDataTypeCriteriaAny;
	}
}
