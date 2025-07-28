package com.nosliw.core.application.uitag;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;

public class HAPUITagDefinitionData extends HAPUITagDefinition{

	@HAPAttribute
	public static final String DATATYPECRITERIA = "dataTypeCriteria";

	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	public HAPUITagDefinitionData() {
	}
	
	@Override
	public String getType() {  return HAPConstantShared.UITAG_TYPE_DATA;    }

	public HAPDataTypeCriteria getDataTypeCriteria() {	return this.m_dataTypeCriteria;	}
	public void setDataTypeCriteria(HAPDataTypeCriteria dataTypeCriteria) {    this.m_dataTypeCriteria = dataTypeCriteria;    }
	
}
