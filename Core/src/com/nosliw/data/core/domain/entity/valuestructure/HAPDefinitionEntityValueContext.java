package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public class HAPDefinitionEntityValueContext extends HAPDefinitionEntityInDomainSimple implements HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private List<HAPDefinitionWrapperValueStructure> m_valueStructures;
	
	public HAPDefinitionEntityValueContext() {
		this.m_valueStructures = new ArrayList<HAPDefinitionWrapperValueStructure>();
	}
	
	public List<HAPDefinitionWrapperValueStructure> getValueStructures(){   return this.m_valueStructures;  }
	public void addValueStructure(HAPDefinitionWrapperValueStructure valueStructure) {    this.m_valueStructures.add(valueStructure);    }
	
	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext,	HAPParserDataExpression expressionParser) {
		for(HAPDefinitionWrapperValueStructure valueStructure : this.m_valueStructures) {
			valueStructure.discoverConstantScript(complexEntityId, parserContext, expressionParser);
		}
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPDefinitionWrapperValueStructure part : this.m_valueStructures) {
			valueStructureJsonArray.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(valueStructureJsonArray.toArray(new String[0])));
	}
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityValueContext out = new HAPDefinitionEntityValueContext();
		for(HAPDefinitionWrapperValueStructure valueStructure : this.m_valueStructures) {
			out.m_valueStructures.add(valueStructure.cloneValueStructureWrapper());
		}
		return out;
	}

}
