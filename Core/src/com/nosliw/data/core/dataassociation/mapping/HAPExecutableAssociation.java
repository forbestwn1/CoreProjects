package com.nosliw.data.core.dataassociation.mapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

@HAPEntityWithAttribute
public class HAPExecutableAssociation extends HAPExecutableImp{

	@HAPAttribute
	public static String CONTEXT = "context";
	
	@HAPAttribute
	public static String PATHMAPPING = "pathMapping";

	@HAPAttribute
	public static String INPUT = "input";

	@HAPAttribute
	public static String FLATINPUT = "flatInput";

	@HAPAttribute
	public static String OUTPUT = "output";

	@HAPAttribute
	public static String FLATOUTPUT = "flatOutput";

	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	@HAPAttribute
	public static String CONVERTFUNCTION = "convertFunction";
	
	//input structure
	private Map<String, HAPValueStructure> m_input;
	private Map<String, Boolean> m_isFlatInput;
	
	//output
	private HAPValueStructure m_output;

	//data association output context
	private HAPValueMapping m_mapping;
	
	//path mapping for relative node (output path in context - input path in context) during runtime
	private Map<String, String> m_relativePathMapping;
	
	private Map<String, Object> m_constantAssignment;
	
	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	public HAPExecutableAssociation(HAPContainerStructure input, HAPValueStructure output) {
		this();
		for(String inputName : input.getStructureNames())  this.addInputStructure(inputName, (HAPValueStructure)input.getStructure(inputName).cloneStructure()); 
		this.m_output = output;
	}

	public HAPExecutableAssociation() {
		this.m_outputMatchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_input = new LinkedHashMap<String, HAPValueStructure>();
		this.m_isFlatInput = new LinkedHashMap<String, Boolean>();
		this.m_relativePathMapping = new LinkedHashMap<String, String>();
	}

	public void addInputStructure(String name, HAPValueStructure structure) {  
		this.m_input.put(name, structure);
		this.m_isFlatInput.put(name, structure.isFlat());
	}
	public Map<String, Boolean> isFlatInput(){  return this.m_isFlatInput;  }
	public boolean isFlatInput(String inputName) {   return this.m_input.get(inputName).isFlat();  }
	
	public boolean isFlatOutput() {   return this.m_output.isFlat();  }
	public HAPValueStructure getOutputContext() {
		HAPValueStructure out = null;
		switch(this.m_output.getStructureType()) {
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY:
//			out = this.m_mapping;
			break;
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT:
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT:
			out = this.m_output;
			break;
		}
		return out;
	}
	
	
	public void setMapping(HAPValueMapping mapping) {   this.m_mapping = mapping.cloneValueMapping();   }
	public HAPValueMapping getMapping() {   return this.m_mapping;   }
//	public HAPContext getSolidContext() {
//		if(this.m_mapping==null)   return null;
//		return this.m_mapping.toSolidContext();
//	}
	
	public void setConstantAssignments(Map<String, Object> constantAssignment) {     this.m_constantAssignment = constantAssignment;      }
	public Map<String, Object> getConstantAssignments(){    return this.m_constantAssignment;   }

	public void setRelativePathMappings(Map<String, String> mapping) {    this.m_relativePathMapping = mapping;    }
	public Map<String, String> getRelativePathMappings() {  return this.m_relativePathMapping;  }

	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputMatchers.put(path, matchers);     }
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputMatchers; }

	//update output root name
	public void updateOutputRootName(HAPUpdateName nameUpdate) {
		//update path mapping
		Map<String, String> processedPathMapping = new LinkedHashMap<String, String>();

		for(String p1 : m_relativePathMapping.keySet()) {
			HAPReferenceElement cPath = new HAPReferenceElement(p1);
//			HAPReferenceElement cPath1 = new HAPReferenceElement(new HAPIdContextDefinitionRoot(nameUpdate.getUpdatedName(cPath.getRootReference().getFullName())), cPath.getSubPath());
//			processedPathMapping.put(cPath1.getFullPath(), m_relativePathMapping.get(p1));
		}
		this.m_relativePathMapping = processedPathMapping;

		//update context
//		this.m_mapping.updateRootName(nameUpdate);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, this.m_mapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATHMAPPING, HAPJsonUtility.buildMapJson(m_relativePathMapping));

		jsonMap.put(FLATOUTPUT, this.isFlatOutput()+"");
		typeJsonMap.put(FLATOUTPUT, Boolean.class);
		jsonMap.put(OUTPUT, this.m_output.toStringValue(HAPSerializationFormat.JSON));
		
		jsonMap.put(FLATINPUT, HAPJsonUtility.buildJson(this.isFlatInput(), HAPSerializationFormat.JSON));
		jsonMap.put(INPUT, HAPJsonUtility.buildJson(this.m_input, HAPSerializationFormat.JSON));
		
		if(this.m_outputMatchers!=null && !this.m_outputMatchers.isEmpty()) {
			jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
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
			jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
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
