package com.nosliw.uiresource.definition;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * Store information for node context
 * For now, the information is data type criteria for the node 
 */
@HAPEntityWithAttribute
public class HAPContextNodeDefinition implements HAPSerializable{
	
	@HAPAttribute
	public static final String DEFAULT = "default";
	
	private HAPDataTypeCriteria m_criteria;

	public HAPContextNodeDefinition(HAPDataTypeCriteria criteria){
		this.m_criteria = criteria;
	}

	public HAPDataTypeCriteria getValue(){
		return this.m_criteria;
	}

	@Override
	public String toStringValue(HAPSerializationFormat format) {
		return HAPSerializeManager.getInstance().toStringValue(m_criteria, HAPSerializationFormat.LITERATE);
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		return false;
	}
	
}
