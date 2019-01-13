package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextPath;

@HAPEntityWithAttribute
public class HAPExecutableDataAssociationGroup extends HAPSerializableImp implements HAPExecutable{

	@HAPAttribute
	public static String DEFINITION = "definition";

	@HAPAttribute
	public static String CONTEXT = "context";
	
	@HAPAttribute
	public static String PATHMAPPING = "pathMapping";

	@HAPAttribute
	public static String CONVERTFUNCTION = "convertFunction";
	
	@HAPAttribute
	public static String FLATOUTPUT = "flatOutput";

	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	private HAPDefinitionDataAssociationGroup m_definition;
	
	//process purpose
	private HAPContextFlat m_context;
	
	//mapping from in path to out path, it is for runtime 
	private Map<String, String> m_pathMapping;
	
	private Map<String, HAPMatchers> m_outputMatchers;
	
	public HAPExecutableDataAssociationGroup(HAPDefinitionDataAssociationGroup definition) {
		this.m_definition = definition;
		this.m_outputMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	public HAPInfo getInfo() {  return this.m_definition.getInfo();  }
	
	public HAPContextFlat getContext() {   return this.m_context;   }
	public void setContext(HAPContextFlat context) {   this.m_context = context;   }

	public void setPathMapping(Map<String, String> mapping) {    this.m_pathMapping = mapping;    }
	public Map<String, String> getPathMapping() {  return this.m_pathMapping;  }

	public boolean isFlatOutput() {   return this.m_definition.isFlatOutput();  }
	
	public void addMatchers(String path, HAPMatchers matchers) {		this.m_outputMatchers.put(path, matchers);	}
	
	//update output root name
	public void updateOutputRootName(HAPUpdateName nameUpdate) {
		//update path mapping
		Map<String, String> processedPathMapping = new LinkedHashMap<String, String>();
		for(String p1 : m_pathMapping.keySet()) {
			HAPContextPath cPath = new HAPContextPath(m_pathMapping.get(p1));
			HAPContextPath cPath1 = new HAPContextPath(new HAPContextDefinitionRootId(nameUpdate.getUpdatedName(cPath.getRootElementId().getFullName())), cPath.getSubPath());
			processedPathMapping.put(p1, cPath1.getFullPath());
		}
		this.m_pathMapping = processedPathMapping;

		//update context
		this.m_context.updateRootName(nameUpdate);
		
		//update matchers
		Map<String, HAPMatchers> outputMatchers = new LinkedHashMap<String, HAPMatchers>();
		for(String p1 :this.m_outputMatchers.keySet()) {
			HAPContextPath cPath = new HAPContextPath(p1);
			cPath = new HAPContextPath(new HAPContextDefinitionRootId(nameUpdate.getUpdatedName(cPath.getRootElementId().getFullName())), cPath.getSubPath());
			outputMatchers.put(cPath.getFullPath(), this.m_outputMatchers.get(p1));
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATHMAPPING, HAPJsonUtility.buildMapJson(m_pathMapping));
		jsonMap.put(FLATOUTPUT, this.isFlatOutput()+"");
		typeJsonMap.put(FLATOUTPUT, Boolean.class);
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		jsonMap.put(CONVERTFUNCTION, HAPUtilityScript.buildDataAssociationConvertFunction(this).getScript());
		typeJsonMap.put(CONVERTFUNCTION, HAPScript.class);
		
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}
		

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		return null;
	}
}
