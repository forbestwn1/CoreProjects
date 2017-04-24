package com.nosliw.data.core.imp.criteria;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPDataTypeCriteriaWrapperLiterate extends HAPSerializableImp implements HAPDataTypeCriteria{

	private String m_literateValue;
	
	private HAPDataTypeCriteria m_criteria;

	public HAPDataTypeCriteriaWrapperLiterate(){
	}

	public String getLiterateValue(){		return this.m_literateValue;	}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_LITERATE;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {
		return this.getCriteria().validate(criteria, dataTypeHelper);
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {
		return this.getCriteria().validate(dataTypeId, dataTypeHelper);
	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		return this.getCriteria().getValidDataTypeId(dataTypeHelper);
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
	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
		return this.getCriteria().normalize(dataTypeHelper);
	}

	private HAPDataTypeCriteria getCriteria(){
		if(this.m_criteria==null){
			this.m_criteria = HAPDataTypeCriteriaParser.parseLiterate(this.m_literateValue);
		}
		return this.m_criteria;
	}
	
}
