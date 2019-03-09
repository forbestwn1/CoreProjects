package com.nosliw.data.core.script.context.dataassociation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;

public class HAPExecutableAssociation extends HAPExecutableImp{

	@HAPAttribute
	public static String CONTEXT = "context";
	
	@HAPAttribute
	public static String PATHMAPPING = "pathMapping";

	@HAPAttribute
	public static String CONVERTFUNCTION = "convertFunction";
	
	@HAPAttribute
	public static String FLATINPUT = "flatInput";

	@HAPAttribute
	public static String FLATOUTPUT = "flatOutput";

	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	//input structure
	private Map<String, HAPContextStructure> m_input;
	private Map<String, Boolean> m_isFlatInput;
	
	//output
	private HAPContextStructure m_output;

	private HAPContext m_definition;
	
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
		this.m_definition = definition;
	}

	public HAPExecutableAssociation() {
		this.m_outputMatchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_input = new LinkedHashMap<String, HAPContextStructure>();
	}

	public void addInputStructure(String name, HAPContextStructure structure) {  
		this.m_input.put(name, structure);
		this.m_isFlatInput.put(name, structure.isFlat());
	}
	public Map<String, Boolean> isFlatInput(){  return this.m_isFlatInput;  }
	public boolean isFlatInput(String inputName) {   return this.m_input.get(inputName).isFlat();  }
	
	public boolean isFlatOutput() {   return this.m_output.isFlat();  }
	public HAPContextStructure getOutputContext() {
		HAPContext out = null;
		switch(this.m_output.getType()) {
		case HAPConstant.CONTEXTSTRUCTURE_TYPE_EMPTY:
			out = this.m_mapping;
			break;
		case HAPConstant.CONTEXTSTRUCTURE_TYPE_FLAT:
		case HAPConstant.CONTEXTSTRUCTURE_TYPE_NOTFLAT:
			out = (HAPContext)this.m_output;
			break;
		}
		return out;
	}
	
	
	public void setMapping(HAPContext context) {   this.m_mapping = context;   }
	public HAPContext getMapping() {   return this.m_mapping;   }
	public HAPContext getSolidContext() {
		if(this.m_mapping==null)   return null;
		return this.m_mapping.toSolidContext();
	}

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
		jsonMap.put(FLATINPUT, HAPJsonUtility.buildJson(this.isFlatInput(), HAPSerializationFormat.JSON));
		if(this.m_outputMatchers!=null && !this.m_outputMatchers.isEmpty()) {
			jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
		}
	}

//	@Override
//	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
//		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
//		jsonMap.put(CONVERTFUNCTION, HAPUtilityScript.buildDataAssociationConvertFunction(this).getScript());
//		typeJsonMap.put(CONVERTFUNCTION, HAPScript.class);
//	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_outputMatchers!=null && !this.m_outputMatchers.isEmpty()) {
			jsonMap.put(OUTPUTMATCHERS, HAPJsonUtility.buildJson(m_outputMatchers, HAPSerializationFormat.JSON));
		}
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependent> dependency, HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceId> ids = new ArrayList<HAPResourceId>();
		for(String name : this.m_outputMatchers.keySet()) {
			ids.addAll(HAPMatcherUtility.getMatchersResourceId(this.m_outputMatchers.get(name)));
		}
		dependency.addAll(HAPResourceUtility.buildResourceDependentFromResourceId(ids));
	}
}
