package com.nosliw.uiresource.page.story.model;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

//
public class HAPUIDataInfo extends HAPSerializableImp{

	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	public HAPDataTypeCriteria getDataTypeCriteria() {	return this.m_dataTypeCriteria;	}
	public void setDataTypeCriteria(HAPDataTypeCriteria dataTypeCriteria) {    this.m_dataTypeCriteria = dataTypeCriteria;      }

}
