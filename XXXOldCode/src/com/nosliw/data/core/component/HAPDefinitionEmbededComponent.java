package com.nosliw.data.core.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.event.HAPDefinitionHandlerEvent;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForComponent;

//a component reference embeded
public class HAPDefinitionEmbededComponent extends HAPEntityInfoWritableImp implements HAPWithNameMapping, HAPWithEventHanlder{

	@HAPAttribute
	public static String NAMEMAPPING = "nameMapping";

	@HAPAttribute
	public static final String IN = "in";
	
	@HAPAttribute
	public static final String OUT = "out";
	
	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";

	//mapping reference name from attachment to internal name
	private HAPNameMapping m_nameMapping;

	private HAPDefinitionGroupDataAssociationForComponent m_outDataAssociations;
	
	private HAPDefinitionGroupDataAssociationForComponent m_inDataAssociations;
	
	//event handlers
	private Set<HAPDefinitionHandlerEvent> m_eventHandlers;
	
	public HAPDefinitionEmbededComponent() {
		this.m_nameMapping = new HAPNameMapping();
		this.m_eventHandlers = new HashSet<HAPDefinitionHandlerEvent>();
		this.m_outDataAssociations = new HAPDefinitionGroupDataAssociationForComponent();
		this.m_inDataAssociations = new HAPDefinitionGroupDataAssociationForComponent();
	}
	
	public void setNameMapping(HAPNameMapping nameMapping) {   if(nameMapping!=null)  this.m_nameMapping = nameMapping;  }
	@Override
	public HAPNameMapping getNameMapping() {    return this.m_nameMapping;   }

	@Override
	public Set<HAPDefinitionHandlerEvent> getEventHandlers(){   return this.m_eventHandlers;   }
	@Override
	public void addEventHandler(HAPDefinitionHandlerEvent eventHandler) {  this.m_eventHandlers.add(eventHandler);   }

	public HAPDefinitionGroupDataAssociationForComponent getInDataAssociations() {   return this.m_inDataAssociations;   }
	public void setInDataAssociations(HAPDefinitionGroupDataAssociationForComponent dataAssociations) {   this.m_inDataAssociations = dataAssociations;   }
	public void addInDataAssociation(HAPDefinitionDataAssociation dataAssociation) {   this.m_inDataAssociations.addDataAssociation(dataAssociation);  }

	public HAPDefinitionGroupDataAssociationForComponent getOutDataAssociations() {   return this.m_outDataAssociations;    }
	public void setOutDataAssociations(HAPDefinitionGroupDataAssociationForComponent dataAssociations) {   this.m_outDataAssociations = dataAssociations;   }
	public void addOutDataAssociation(HAPDefinitionDataAssociation dataAssociation) {   this.m_outDataAssociations.addDataAssociation(dataAssociation);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(IN, this.m_inDataAssociations.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUT, this.m_outDataAssociations.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPUtilityJson.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_inDataAssociations.buildObject(jsonObj.optJSONArray(IN), HAPSerializationFormat.JSON);
		this.m_outDataAssociations.buildObject(jsonObj.optJSONArray(OUT), HAPSerializationFormat.JSON);
		HAPParserEntityComponent.parseEventHandler(this, jsonObj);
		return true;
	}

	protected void cloneToEmbededComponent(HAPDefinitionEmbededComponent embededComponent) {
		this.cloneToEntityInfo(embededComponent);
		for(HAPDefinitionHandlerEvent eventHandler : this.m_eventHandlers) {
			embededComponent.addEventHandler(eventHandler.cloneEventHandler());
		}
		if(this.m_nameMapping==null)   embededComponent.m_nameMapping = null;
		else   embededComponent.m_nameMapping = this.m_nameMapping.cloneNameMapping();
		embededComponent.m_inDataAssociations = this.m_inDataAssociations.cloneGroupDataAssociation();
		embededComponent.m_outDataAssociations = this.m_outDataAssociations.cloneGroupDataAssociation();
	}
	
}
