package com.nosliw.uiresource.module;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociationGroup;

//each module ui is page unit in module that is alive in a module
//as it defined:
//		what it look like
//		where data come from: service provider
//		how to interact with page : page event handler
@HAPEntityWithAttribute
public class HAPDefinitionModuleUI extends HAPEntityInfoWritableImp{

	public static String STATUS_DISABLED = "disabled";
	
	@HAPAttribute
	public static String PAGE = "page";
	
	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	@HAPAttribute
	public static String OUTPUTMAPPING = "outputMapping";

	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String STATUS = "status";
	
	//ui page
	private String m_page;

	//event handlers
	private Map<String, HAPDefinitionModuleUIEventHander> m_eventHandlers;
	
	//data mapping (from data definition in module to public data definition in page)
	private HAPDefinitionDataAssociationGroup m_inputMapping;
	private HAPDefinitionDataAssociationGroup m_outputMapping;
	
	//provide extra information about this module ui so that container can render it properly
	private String m_type;
	
	private String m_status;
	
	public HAPDefinitionModuleUI() {
		this.m_eventHandlers = new LinkedHashMap<String, HAPDefinitionModuleUIEventHander>();
		this.m_inputMapping = new HAPDefinitionDataAssociationGroup();
		this.m_outputMapping = new HAPDefinitionDataAssociationGroup();
	}
	
	public String getPage() {   return this.m_page;    }
	public void setPage(String page) {   this.m_page = page;   }

	public String getType() {   return this.m_type;    }
	public void setType(String type) {   this.m_type = type;   }

	public String getStatus() {   return this.m_status;    }
	public void setStatus(String status) {   this.m_status = status;   }

	public HAPDefinitionDataAssociationGroup getInputMapping() {   return this.m_inputMapping;   }
	public void setInputMapping(HAPDefinitionDataAssociationGroup contextMapping) {   this.m_inputMapping = contextMapping;   }

	public HAPDefinitionDataAssociationGroup getOutputMapping() {   return this.m_outputMapping;   }
	public void setOutputMapping(HAPDefinitionDataAssociationGroup contextMapping) {   this.m_outputMapping = contextMapping;   }

	public Map<String, HAPDefinitionModuleUIEventHander> getEventHandlers(){   return this.m_eventHandlers;   }
	public void addEventHandler(String name, HAPDefinitionModuleUIEventHander eventHandler) {  this.m_eventHandlers.put(name, eventHandler);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PAGE, this.m_page);
		jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}
}
