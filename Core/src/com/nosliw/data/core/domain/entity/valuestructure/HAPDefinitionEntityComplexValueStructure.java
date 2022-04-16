package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;

public class HAPDefinitionEntityComplexValueStructure extends HAPDefinitionEntityInDomainSimple{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX;

	public static final String PART = "part";
	
	private List<HAPWrapperValueStructureDefinition> m_parts;
	
	public HAPDefinitionEntityComplexValueStructure() {
		this.m_parts = new ArrayList<HAPWrapperValueStructureDefinition>();
	}
	
	public List<HAPWrapperValueStructureDefinition> getParts(){   return this.m_parts;  }
	public void addPart(HAPWrapperValueStructureDefinition part) {    this.m_parts.add(part);    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> partJsonArray = new ArrayList<String>();
		for(HAPWrapperValueStructureDefinition part : this.m_parts) {
			partJsonArray.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPJsonUtility.buildArrayJson(partJsonArray.toArray(new String[0])));
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinition entityDefDomain){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> partJsonArray = new ArrayList<String>();
		for(HAPWrapperValueStructureDefinition part : this.m_parts) {
			partJsonArray.add(part.toExpandedJsonString(entityDefDomain));
		}
		jsonMap.put(PART, HAPJsonUtility.buildArrayJson(partJsonArray.toArray(new String[0])));
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexValueStructure out = new HAPDefinitionEntityComplexValueStructure();
		for(HAPWrapperValueStructureDefinition part : this.m_parts) {
			out.m_parts.add(part.cloneValueStructureWrapper());
		}
		return out;
	}
}
