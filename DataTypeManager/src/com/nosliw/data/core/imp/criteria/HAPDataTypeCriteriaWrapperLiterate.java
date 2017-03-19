package com.nosliw.data.core.imp.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaAnd;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementRange;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public class HAPDataTypeCriteriaWrapperLiterate extends HAPSerializableImp implements HAPDataTypeCriteria{

	private String m_literateValue;
	
	private HAPDataTypeCriteria m_criteria;

	private HAPDataTypeCriteriaManagerImp m_dataTypeCriteriaMan;

	public HAPDataTypeCriteriaWrapperLiterate(HAPDataTypeCriteriaManagerImp criteriaMan){
		this.m_dataTypeCriteriaMan = criteriaMan;
	}

	
	public String getLiterateValue(){		return this.m_literateValue;	}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_LITERATE;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria) {
		return this.getCriteria().validate(criteria);
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId) {
		return this.getCriteria().validate(dataTypeId);
	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		return this.getCriteria().getValidDataTypeId();
	}

	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.m_literateValue = literateValue;
		return true;
	}

	@Override
	protected String buildLiterate(){
		if(this.m_literateValue==null){
			HAPSerializeManager.getInstance().toStringValue(this.m_criteria, HAPSerializationFormat.LITERATE);
		}
		return this.m_literateValue;
	}

	@Override
	public HAPDataTypeCriteria normalize() {
		return this.getCriteria().normalize();
	}

	private HAPDataTypeCriteria getCriteria(){
		if(this.m_criteria==null){
			this.m_criteria = HAPDataTypeCriteriaParser.parseLiterate(this.m_literateValue, m_dataTypeCriteriaMan);
		}
		return this.m_criteria;
	}
	
}
