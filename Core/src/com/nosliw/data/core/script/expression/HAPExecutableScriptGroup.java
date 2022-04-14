package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpression;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableScriptGroup extends HAPExecutableImp{

	@HAPAttribute
	public static final String EXPRESSIONGROUP = "expressionGroup";

	@HAPAttribute
	public static final String ELEMENT = "element";

	private HAPValueStructureWrapper m_valueStructureWrapper;
	
	private HAPExecutableExpressionGroup m_expressionExe;
	
	private List<HAPExecutableScriptEntity> m_elements;
	
//	private Map<String, Object> m_constants;
	
	public HAPExecutableScriptGroup() {
		this.m_elements = new ArrayList<HAPExecutableScriptEntity>();
//		this.m_constants = new LinkedHashMap<String, Object>();
	}
	
	public void setValueStructureDefinitionWrapper(HAPValueStructureWrapper valueStructureWrapper) {   	this.m_valueStructureWrapper = valueStructureWrapper;	}
	
	public HAPValueStructureWrapper getValueStructureDefinitionWrapper() {    return this.m_valueStructureWrapper;    }
	
	public HAPExecutableExpressionGroup getExpression() {    return this.m_expressionExe;   }
	public void setExpression(HAPExecutableExpressionGroup expression) {    this.m_expressionExe = expression;    }
	
//	public Map<String, Object> getConstantsValue(){    return this.m_constants;     }
//	public void addConstants(Map<String, Object> constants) {  this.m_constants.putAll(constants);    }
	
	public void updateConstant(Map<String, Object> constants) {
		for(HAPExecutableScriptEntity scriptExe : this.m_elements) {
			scriptExe.updateConstant(constants);
		}
		
		for(HAPExecutableExpression expressionExe : this.m_expressionExe.getExpressionItems().values()) {
			expressionExe.updateConstant(constants);
		}
	}
	
	public Set<HAPDefinitionConstant> getConstantsDefinition() {
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>(); 
		for(HAPExecutableScriptEntity scriptExe : this.m_elements) {
			HAPUtilityScriptExpression.addConstantDefinition(out, scriptExe.discoverConstantsDefinition(null));
		}
		
		for(HAPExecutableExpression expressionExe : this.m_expressionExe.getExpressionItems().values()) {
			HAPUtilityScriptExpression.addConstantDefinition(out, expressionExe.getConstantsDefinition());
		}
		return new HashSet<HAPDefinitionConstant>(out.values());
	}
	
	public void addScript(HAPExecutableScriptEntity script) {   this.m_elements.add(script);    }
	public List<HAPExecutableScriptEntity> getScripts(){    return this.m_elements;    }
	public HAPExecutableScriptEntity getScript(Object id) {
		HAPExecutableScriptEntity out = null;
		if(id==null)  id = HAPConstantShared.NAME_DEFAULT;
		if(id instanceof String) {
			for(HAPExecutableScriptEntity script : this.m_elements) {
				if(id.equals(script.getId())) {
					out = script;
					break;
				}
			}
		}
		else if(id instanceof Integer) {
			int index = (Integer)id;
			out = this.m_elements.get(index);
		}
		return out;
	}
	
	public Set<String> discoverVariables(){
		Set<String> out = new HashSet<String>();
		for(HAPExecutableScriptEntity scriptEntity : this.m_elements) {
			out.addAll(scriptEntity.discoverVariables(this.m_expressionExe));
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(EXPRESSIONGROUP, this.m_expressionExe.toStringValue(HAPSerializationFormat.JSON));
		Map<String, String> elementJsonMap = new LinkedHashMap<String, String>();
		for(HAPExecutableScriptEntity ele : this.m_elements) {
			elementJsonMap.put(ele.getId(), ele.toStringValue(HAPSerializationFormat.JSON));
		}

		Map<String, String> eleMap = new LinkedHashMap<String, String>();
		for(HAPExecutableScriptEntity script : this.m_elements) {
			eleMap.put(script.getId(), script.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ELEMENT, HAPJsonUtility.buildMapJson(elementJsonMap));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		jsonMap.put(EXPRESSIONGROUP, this.m_expressionExe.toResourceData(runtimeInfo).toString());
		Map<String, String> elementJsonMap = new LinkedHashMap<String, String>();
		for(HAPExecutableScriptEntity ele : this.m_elements) {
			elementJsonMap.put(ele.getId(), ele.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ELEMENT, HAPJsonUtility.buildMapJson(elementJsonMap));
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> deps = this.m_expressionExe.getResourceDependency(runtimeInfo, resourceManager);
		dependency.addAll(deps);
	}
}
