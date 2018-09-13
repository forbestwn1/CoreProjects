package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;
import com.nosliw.uiresource.processor.HAPProcessorUIResource;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceManager {

	private Map<String, HAPExecutableUIUnitResource> m_uiResource;
	
	private HAPExpressionSuiteManager m_expressionMan; 
	
	private HAPResourceManagerRoot m_resourceMan;

	private HAPUITagManager m_uiTagMan;
	
	private HAPRuntime m_runtime;

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPIdGenerator m_idGengerator = new HAPIdGenerator(1);

	public HAPUIResourceManager(
			HAPUITagManager uiTagMan,
			HAPExpressionSuiteManager expressionMan, 
			HAPResourceManagerRoot resourceMan, 
			HAPRuntime runtime, 
			HAPDataTypeHelper dataTypeHelper){
		this.m_uiTagMan = uiTagMan;
		this.m_expressionMan = expressionMan;
		this.m_resourceMan = resourceMan;
		this.m_runtime = runtime;
		this.m_uiResource = new LinkedHashMap<String, HAPExecutableUIUnitResource>();
		this.m_dataTypeHelper = dataTypeHelper;
	}

	public HAPExecutableUIUnitResource getUIResource(String id){
		HAPExecutableUIUnitResource out = this.m_uiResource.get(id);
		if(out==null) {
			HAPDefinitionUIUnitResource def = getUIResourceDefinitionById(id);
			out = this.processUIResource(def);
		}

		System.out.println();
		System.out.println();
		System.out.println("*********************** UI Resource ************************");
		String content = out.toStringValue(HAPSerializationFormat.JSON);
		content = HAPJsonUtility.formatJson(content);
		System.out.println(content);
		System.out.println("*********************** UI Resource ************************");
		System.out.println();
		System.out.println();
		
		return out;
	}
	
    //Add resource definition from file 
	public HAPExecutableUIUnitResource addUIResourceDefinition(String file){
		HAPDefinitionUIUnitResource def = this.readUiResourceDefinitionFromFile(file);
		HAPExecutableUIUnitResource exeUiResource = this.processUIResource(def);
		this.m_uiResource.put(exeUiResource.getUIUnitDefinition().getId(), exeUiResource);
		return exeUiResource;
	}
	
	public HAPDefinitionUIUnitResource getUIResourceDefinitionById(String id){
		String file = HAPFileUtility.getUIResourceFolder()+id+".res";
		HAPDefinitionUIUnitResource uiResourceDef = this.readUiResourceDefinitionFromFile(file);
		uiResourceDef = this.getUIResourceParser().processInclude(uiResourceDef, this);
		return uiResourceDef;
	}
	
	private HAPExecutableUIUnitResource processUIResource(HAPDefinitionUIUnitResource uiResource) {
		return HAPProcessorUIResource.processUIResource(uiResource, this, m_dataTypeHelper, m_uiTagMan, m_runtime, m_expressionMan, m_resourceMan, this.getUIResourceParser(), m_idGengerator);
	}
	
	private HAPDefinitionUIUnitResource readUiResourceDefinitionFromFile(String file) {
		HAPDefinitionUIUnitResource uiResource = this.getUIResourceParser().parseFile(file);
		return uiResource;
	}
	
	private HAPParserUIResource getUIResourceParser(){
		HAPParserUIResource uiResourceParser = new HAPParserUIResource(null, m_idGengerator);
		return uiResourceParser;
	}
}
