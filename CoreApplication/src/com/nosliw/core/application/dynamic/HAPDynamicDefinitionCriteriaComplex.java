package com.nosliw.core.application.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDynamicDefinitionCriteriaComplex extends HAPDynamicDefinitionCriteria{

	public final static String CHILDREN = "children"; 

	private List<HAPDynamicDefinitionCriteria> m_children;

	public HAPDynamicDefinitionCriteriaComplex() {
		super(HAPConstantShared.DYNAMICDEFINITION_CRITERIA_COMPLEX);
		this.m_children = new ArrayList<HAPDynamicDefinitionCriteria>();
	}

	public List<HAPDynamicDefinitionCriteria> getChildren(){	return this.m_children;	}

	public void addChild(HAPDynamicDefinitionCriteria child) {   this.m_children.add(child);     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHILDREN, HAPManagerSerialize.getInstance().toStringValue(m_children, HAPSerializationFormat.JSON));
	}

}
