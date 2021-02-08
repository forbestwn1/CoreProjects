package com.nosliw.data.core.script.context.dataassociation.mapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPResourceUtility;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPParserContext;

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
	private Map<String, HAPContextStructure> m_input;
	private Map<String, Boolean> m_isFlatInput;
	
	//output
	private HAPContextStructure m_output;

	//data association output context
	private HAPContext m_mapping;
	
	//path mapping (output path in context - input path in context) during runtime
	private Map<String, String> m_pathMapping;
	
	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	public HAPExecutableAssociation(HAPParentContext input, HAPContext definition, HAPContextStructure output) {
		this();
		for(String inputName : input.getNames())  this.addInputStructure(inputName, input.getContext(inputName).cloneContextStructure()); 
		this.m_output = output;
	}

	public HAPExecutableAssociation() {
		this.m_outputMatchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_input = new LinkedHashMap<String, HAPContextStructure>();
		this.m_isFlatInput = new LinkedHashMap<String, Boolean>();
		this.m_pathMapping = new LinkedHashMap<String, String>();
	}

	public void addInputStructure(String name, HAPContextStructure structure) {  
		this.m_input.put(name, structure);
		this.m_isFlatInput.put(name, structure.isFlat());
	}
	public Map<String, Boolean> isFlatInput(){  return this.m_isFlatInput;  }
	public boolean isFlatInput(String inputName) {   return this.m_input.get(inputName).isFlat();  }
	
	public boolean isFlatOutput() {   return this.m_output.isFlat();  }
	public HAPContextStructure getOutputContext() {
		HAPContextStructure out = null;
		switch(this.m_output.getType()) {
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY:
			out = this.m_mapping.toSolidContext();
			break;
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT:
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT:
			out = this.m_output;
			break;
		}
		return out;
	}
	
	
	public void setMapping(HAPContext context) {   this.m_mapping = context.cloneContext();   }
	public HAPContext getMapping() {   return this.m_mapping;   }
//	public HAPContext getSolidContext() {
//		if(this.m_mapping==null)   return null;
//		return this.m_mapping.toSolidContext();
//	}

	public void setPathMapping(Map<String, String> mapping) {    this.m_pathMapping = mapping;    }
	public Map<String, String> getPathMapping() {  return this.m_pathMapping;  }

	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputMatchers.put(path, matchers);     }
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputMatchers; }

	//update output root name
	public void updateOutputRootName(HAPUpdateName nameUpdate) {
		//update path mapping
		Map<String, String> processedPathMapping = new LinkedHashMap<String, String>();

		for(String p1 : m_pathMapping.keySet()) {
			HAPContextPath cPath = new HAPContextPath(p1);
			HAPContextPath cPath1 = new HAPContextPath(new HAPContextDefinitionRootId(nameUpdate.getUpdatedName(cPath.getRootElementId().getFullName())), cPath.getSubPath());
			processedPathMapping.put(cPath1.getFullPath(), m_pathMapping.get(p1));
		}
		this.m_pathMapping = processedPathMapping;

		//update context
		this.m_mapping.updateRootName(nameUpdate);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, this.m_mapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATHMAPPING, HAPJsonUtility.buildMapJson(m_pathMapping));

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
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		
		this.m_mapping = new HAPContext();
		this.m_mapping.buildObject(jsonObj.getJSONObject(CONTEXT), HAPSerializationFormat.JSON);
		  
		JSONObject pathMappingJsonObj = jsonObj.getJSONObject(PATHMAPPING);
		for(Object key1 : pathMappingJsonObj.keySet()) {
			this.m_pathMapping.put((String)key1, pathMappingJsonObj.getString((String)key1));
		}
		
		JSONObject inputJson = jsonObj.getJSONObject(INPUT);
		for(Object key2 : inputJson.keySet()) {
			this.m_input.put((String)key2, HAPParserContext.parseContextStructure(inputJson.getJSONObject((String)key2)));
		}
		
		JSONObject flatInputJsonObj = jsonObj.getJSONObject(FLATINPUT);
		for(Object key3 : flatInputJsonObj.keySet()) {
			this.m_isFlatInput.put((String)key3, inputJson.getBoolean((String)key3));
		}

		JSONObject outputJson = jsonObj.getJSONObject(OUTPUT);
		this.m_output = HAPParserContext.parseContextStructure(outputJson);
		
		JSONObject outputMatchersJson = jsonObj.optJSONObject(OUTPUTMATCHERS);
		if(outputMatchersJson!=null) {
			for(Object key4 : outputMatchersJson.keySet()) {
				HAPMatchers matchers = new HAPMatchers();
				matchers.buildObject(outputMatchersJson.getJSONObject((String)key4), HAPSerializationFormat.JSON);
				this.m_outputMatchers.put((String)key4, matchers);
			}
		}
		
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
		dependency.addAll(HAPResourceUtility.buildResourceDependentFromResourceId(ids));
	}
}
