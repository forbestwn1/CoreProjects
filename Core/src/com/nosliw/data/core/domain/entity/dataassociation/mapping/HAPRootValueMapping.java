package com.nosliw.data.core.domain.entity.dataassociation.mapping;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.structure.HAPElementStructure;

@HAPEntityWithAttribute
public class HAPRootValueMapping extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String DEFINITION = "definition";

	//context element definition
	private HAPElementStructure m_definition;

	public HAPRootValueMapping() {}
	
	public HAPRootValueMapping(HAPElementStructure definition) {
		this.m_definition = definition;
	}
	
	public HAPElementStructure getDefinition() {
		return this.m_definition;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
	}
	
	public HAPRootValueMapping cloneValueMappingRoot() {
		HAPRootValueMapping out = new HAPRootValueMapping();
		this.cloneToEntityInfo(out);
		if(this.m_definition!=null)  out.m_definition = this.m_definition.cloneStructureElement();
		return out;
	}
}
