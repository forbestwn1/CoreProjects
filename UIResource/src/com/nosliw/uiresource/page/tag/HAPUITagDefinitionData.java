package com.nosliw.uiresource.page.tag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

public class HAPUITagDefinitionData extends HAPUITagDefinition{

	@HAPAttribute
	public static final String DATATYPECRITERIA = "dataTypeCriteria";

	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	public HAPUITagDefinitionData() {
		super(HAPConstantShared.UITAG_TYPE_DATA);
	}
	
	public HAPDataTypeCriteria getDataTypeCriteria() {	return this.m_dataTypeCriteria;	}
	public void setDataTypeCriteria(HAPDataTypeCriteria dataTypeCriteria) {    this.m_dataTypeCriteria = dataTypeCriteria;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPECRITERIA, this.m_dataTypeCriteria.toStringValue(HAPSerializationFormat.LITERATE));
	}
}
