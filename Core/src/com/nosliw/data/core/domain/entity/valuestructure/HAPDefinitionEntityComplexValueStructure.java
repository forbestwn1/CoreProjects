package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;

public class HAPDefinitionEntityComplexValueStructure extends HAPDefinitionEntityInDomainSimple{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX;

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private List<HAPDefinitionWrapperValueStructure> m_valueStructures;
	
	public HAPDefinitionEntityComplexValueStructure() {
		this.m_valueStructures = new ArrayList<HAPDefinitionWrapperValueStructure>();
	}
	
	public List<HAPDefinitionWrapperValueStructure> getValueStructures(){   return this.m_valueStructures;  }
	public void addValueStructure(HAPDefinitionWrapperValueStructure valueStructure) {    this.m_valueStructures.add(valueStructure);    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPDefinitionWrapperValueStructure part : this.m_valueStructures) {
			valueStructureJsonArray.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildArrayJson(valueStructureJsonArray.toArray(new String[0])));
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinitionGlobal entityDefDomain){
		super.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDefDomain);
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPDefinitionWrapperValueStructure part : this.m_valueStructures) {
			valueStructureJsonArray.add(part.toExpandedJsonString(entityDefDomain));
		}
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildArrayJson(valueStructureJsonArray.toArray(new String[0])));
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexValueStructure out = new HAPDefinitionEntityComplexValueStructure();
		for(HAPDefinitionWrapperValueStructure valueStructure : this.m_valueStructures) {
			out.m_valueStructures.add(valueStructure.cloneValueStructureWrapper());
		}
		return out;
	}
}
