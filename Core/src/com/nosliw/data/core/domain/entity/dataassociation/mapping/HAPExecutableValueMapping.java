package com.nosliw.data.core.domain.entity.dataassociation.mapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.structure.HAPRootStructure;

@HAPEntityWithAttribute
public class HAPExecutableValueMapping extends HAPExecutableImp{

	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	@HAPAttribute
	public static String CONVERTFUNCTION = "convertFunction";
	
	//data association output context
	private Map<HAPIdRootElement, HAPRootStructure> m_items;
	
	//path mapping for relative node (output path in context - input path in context) during runtime
	private Map<String, String> m_relativePathMapping;
	
	private Map<String, Object> m_constantAssignment;
	
	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	public HAPExecutableValueMapping() {
		this.m_outputMatchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_relativePathMapping = new LinkedHashMap<String, String>();
	}

	public void setMapping(HAPDefinitionValueMapping mapping) {   this.m_mapping = mapping.cloneValueMapping();   }
	public HAPDefinitionValueMapping getMapping() {   return this.m_mapping;   }
	
	public void setConstantAssignments(Map<String, Object> constantAssignment) {     this.m_constantAssignment = constantAssignment;      }
	public Map<String, Object> getConstantAssignments(){    return this.m_constantAssignment;   }

	public void setRelativePathMappings(Map<String, String> mapping) {    this.m_relativePathMapping = mapping;    }
	public Map<String, String> getRelativePathMappings() {  return this.m_relativePathMapping;  }

	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputMatchers.put(path, matchers);     }
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputMatchers; }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_outputMatchers!=null && !this.m_outputMatchers.isEmpty()) {
			jsonMap.put(OUTPUTMATCHERS, HAPUtilityJson.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
		}
	}

	@Override
	protected boolean buildObjectByJson(Object json){
//		JSONObject jsonObj = (JSONObject)json;
//		super.buildObjectByJson(json);
//		
//		this.m_mapping = new HAPValueStructureDefinitionFlat();
//		this.m_mapping.buildObject(jsonObj.getJSONObject(CONTEXT), HAPSerializationFormat.JSON);
//		  
//		JSONObject pathMappingJsonObj = jsonObj.getJSONObject(PATHMAPPING);
//		for(Object key1 : pathMappingJsonObj.keySet()) {
//			this.m_relativePathMapping.put((String)key1, pathMappingJsonObj.getString((String)key1));
//		}
//		
//		JSONObject inputJson = jsonObj.getJSONObject(INPUT);
//		for(Object key2 : inputJson.keySet()) {
//			this.m_input.put((String)key2, HAPParserContext.parseValueStructure(inputJson.getJSONObject((String)key2)));
//		}
//		
//		JSONObject flatInputJsonObj = jsonObj.getJSONObject(FLATINPUT);
//		for(Object key3 : flatInputJsonObj.keySet()) {
//			this.m_isFlatInput.put((String)key3, inputJson.getBoolean((String)key3));
//		}
//
//		JSONObject outputJson = jsonObj.getJSONObject(OUTPUT);
//		this.m_output = HAPParserContext.parseValueStructure(outputJson);
//		
//		JSONObject outputMatchersJson = jsonObj.optJSONObject(OUTPUTMATCHERS);
//		if(outputMatchersJson!=null) {
//			for(Object key4 : outputMatchersJson.keySet()) {
//				HAPMatchers matchers = new HAPMatchers();
//				matchers.buildObject(outputMatchersJson.getJSONObject((String)key4), HAPSerializationFormat.JSON);
//				this.m_outputMatchers.put((String)key4, matchers);
//			}
//		}
//		
		return true;  
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_outputMatchers!=null && !this.m_outputMatchers.isEmpty()) {
			jsonMap.put(OUTPUTMATCHERS, HAPUtilityJson.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
		}
		jsonMap.put(CONVERTFUNCTION, HAPUtilityScript.buildAssociationConvertFunction(this).getScript());
		typeJsonMap.put(CONVERTFUNCTION, HAPJsonTypeScript.class);
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceIdSimple> ids = new ArrayList<HAPResourceIdSimple>();
		for(String name : this.m_outputMatchers.keySet()) {
			ids.addAll(HAPMatcherUtility.getMatchersResourceId(this.m_outputMatchers.get(name)));
		}
		dependency.addAll(HAPUtilityResourceId.buildResourceDependentFromResourceId(ids));
	}
}
