package com.nosliw.data.core.domain.entity.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;

//normal expression group
public class HAPDefinitionEntityExpressionGroup extends HAPDefinitionEntityInDomainComplex implements HAPWithEntityElement<HAPDefinitionExpression>{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION;

	private Map<String, HAPDefinitionExpression> m_elements;
	
	private Map<String, HAPDefinitionConstant> m_constantDefinitions;
	
	public HAPDefinitionEntityExpressionGroup() {
		super(ENTITY_TYPE);
		this.m_elements = new LinkedHashMap<String, HAPDefinitionExpression>();
		this.m_constantDefinitions = new LinkedHashMap<String, HAPDefinitionConstant>();
	}
	
	public void addExpression(HAPDefinitionExpression element) {
		this.addEntityElement(element);
	}

	@Override
	public Set<HAPDefinitionExpression> getEntityElements() {  return new HashSet<HAPDefinitionExpression>(this.m_elements.values()); }

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {  return this.m_elements.get(id);  }

	@Override
	public void addEntityElement(HAPDefinitionExpression expression) {  
		HAPUtilityEntityInfo.processEntityId(expression);
		this.m_elements.put(expression.getId(), expression);  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> expressionJsonArray = new ArrayList<String>();
		for(HAPDefinitionExpression expression : this.m_elements.values()) {
			expressionJsonArray.add(expression.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ELEMENT, HAPUtilityJson.buildArrayJson(expressionJsonArray.toArray(new String[0])));
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinitionLocal entityDefDomain){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	
	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {  return new HashSet(this.m_constantDefinitions.values());  }  

	@Override
	public HAPDefinitionConstant getConstantDefinition(String id) {  return this.m_constantDefinitions.get(id);  }

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {  this.m_constantDefinitions.put(constantDef.getId(), constantDef);  }

	@Override
	public HAPDefinitionExpressionGroup1 cloneExpressionGroupDefinition() {
		HAPDefinitionEntityExpressionGroup out = new HAPDefinitionEntityExpressionGroup();
		out.m_valueStructureComplex = this.m_valueStructureComplex.cloneValueStructureComplex();
		for(String id : this.m_elements.keySet()) {
			out.m_elements.put(id, this.m_elements.get(id).cloneDefinitionExpression());
		}
		for(String id : this.m_constantDefinitions.keySet()) {
			out.m_constantDefinitions.put(id, this.m_constantDefinitions.get(id).cloneConstantDefinition());
		}
		return out;
	}

	@Override
	public String getValueStructureTypeIfNotDefined() {  return HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT;  }
}
