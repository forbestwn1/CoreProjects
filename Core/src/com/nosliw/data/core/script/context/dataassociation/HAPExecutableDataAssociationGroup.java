package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextPath;

@HAPEntityWithAttribute
public class HAPExecutableDataAssociationGroup extends HAPExecutableImp{

	@HAPAttribute
	public static String DEFINITION = "definition";

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

	private HAPDefinitionDataAssociationGroup m_definition;
	
	//data association output context
	private HAPContextFlat m_context;
	
	//path mapping (output path in context - input path in context) during runtime
	private Map<String, String> m_pathMapping;
	
	private boolean m_isFlatInput;
	
	//whether output is context structure (flat) or context group structure (not flat)
	private boolean m_isFlatOutput;

	public HAPExecutableDataAssociationGroup(HAPDefinitionDataAssociationGroup definition) {
		this.m_definition = definition;
		this.m_isFlatOutput = this.m_definition.isFlatOutput();
	}
	
	public HAPDefinitionDataAssociationGroup getDefinition() {  return this.m_definition;   }
	public void setDefinition(HAPDefinitionDataAssociationGroup definition) {  this.m_definition = definition;   }
	
	public HAPInfo getInfo() {  return this.m_definition.getInfo();  }
	
	public HAPContextFlat getContext() {   return this.m_context;   }
	public void setContext(HAPContextFlat context) {   this.m_context = context;   }

	public void setPathMapping(Map<String, String> mapping) {    this.m_pathMapping = mapping;    }
	public Map<String, String> getPathMapping() {  return this.m_pathMapping;  }

	public boolean isFlatOutput() {   return this.m_isFlatOutput;  }
	public void setIsFlatOutput(boolean isFlatOutput) {  this.m_isFlatOutput = isFlatOutput;  }

	public boolean isFlatInput() {   return this.m_isFlatInput;  }
	public void setIsFlatInput(boolean isFlatInput) {  this.m_isFlatInput = isFlatInput;  }

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
		this.m_context.updateRootName(nameUpdate);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATHMAPPING, HAPJsonUtility.buildMapJson(m_pathMapping));
		jsonMap.put(FLATOUTPUT, this.isFlatOutput()+"");
		typeJsonMap.put(FLATOUTPUT, Boolean.class);
		jsonMap.put(FLATINPUT, this.isFlatInput()+"");
		typeJsonMap.put(FLATINPUT, Boolean.class);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(CONVERTFUNCTION, HAPUtilityScript.buildDataAssociationConvertFunction(this).getScript());
		typeJsonMap.put(CONVERTFUNCTION, HAPScript.class);
	}

}
