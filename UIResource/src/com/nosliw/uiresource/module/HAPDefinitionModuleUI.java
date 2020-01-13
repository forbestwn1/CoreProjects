package com.nosliw.uiresource.module;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPHandlerEvent;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.component.HAPWithNameMapping;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.uiresource.common.HAPInfoDecoration;

//each module ui is page unit in module that is alive in a module
//as it defined:
//		what it look like
//		where data come from: service provider
//		how to interact with page : page event handler
@HAPEntityWithAttribute
public class HAPDefinitionModuleUI extends HAPEntityInfoWritableImp implements HAPWithNameMapping{

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
	public static String UIDECORATION = "uiDecoration";
	
	@HAPAttribute
	public static String NAMEMAPPING = "nameMapping";
	
	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String STATUS = "status";
	
	//ui page
	private String m_page;

	//event handlers
	private Set<HAPHandlerEvent> m_eventHandlers;

	private List<HAPInfoDecoration> m_uiDecoration;
	
	//data mapping (from data definition in module to public data definition in page)
	private HAPDefinitionDataAssociation m_inputMapping;
	private HAPDefinitionDataAssociation m_outputMapping;

	//mapping reference name from attachment to internal name
	private HAPNameMapping m_nameMapping;
	
	//provide extra information about this module ui so that container can render it properly
	private String m_type;
	
	private String m_status;
	
	public HAPDefinitionModuleUI() {
		this.m_eventHandlers = new HashSet<HAPHandlerEvent>();
		this.m_uiDecoration = new ArrayList<HAPInfoDecoration>();
		this.m_nameMapping = new HAPNameMapping();
	}
	
	public String getPage() {   return this.m_page;    }
	public void setPage(String page) {   this.m_page = page;   }

	public String getType() {   return this.m_type;    }
	public void setType(String type) {   this.m_type = type;   }

	public String getStatus() {   return this.m_status;    }
	public void setStatus(String status) {   this.m_status = status;   }

	public HAPDefinitionDataAssociation getInputMapping() {   return this.m_inputMapping;   }
	public void setInputMapping(HAPDefinitionDataAssociation contextMapping) {   this.m_inputMapping = contextMapping;   }

	public HAPDefinitionDataAssociation getOutputMapping() {   return this.m_outputMapping;   }
	public void setOutputMapping(HAPDefinitionDataAssociation contextMapping) {   this.m_outputMapping = contextMapping;   }

	public Set<HAPHandlerEvent> getEventHandlers(){   return this.m_eventHandlers;   }
	public void addEventHandler(HAPHandlerEvent eventHandler) {  this.m_eventHandlers.add(eventHandler);   }
	public void addEventHandler(Set<HAPHandlerEvent> eventHandler) {  this.m_eventHandlers.addAll(eventHandler);   }
	
	public void setUIDecoration(List<HAPInfoDecoration> decs) {  this.m_uiDecoration = decs;    }
	public List<HAPInfoDecoration> getUIDecoration(){  return this.m_uiDecoration;   }
	
	public void setNameMapping(HAPNameMapping nameMapping) {   if(nameMapping!=null)  this.m_nameMapping = nameMapping;  }
	@Override
	public HAPNameMapping getNameMapping() {    return this.m_nameMapping;   }
	 
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PAGE, this.m_page);
		jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
		jsonMap.put(UIDECORATION, HAPJsonUtility.buildJson(this.m_uiDecoration, HAPSerializationFormat.JSON));
	}
}
