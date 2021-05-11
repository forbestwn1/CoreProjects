package com.nosliw.data.core.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociation;
import com.nosliw.data.core.handler.HAPHandler;

//a component reference embeded
public class HAPEmbededComponent extends HAPEntityInfoWritableImp implements HAPWithNameMapping, HAPWithEventHanlder{

	@HAPAttribute
	public static String NAMEMAPPING = "nameMapping";

	@HAPAttribute
	public static final String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static final String OUTPUTMAPPING = "outputMapping";
	
	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";


	//mapping reference name from attachment to internal name
	private HAPNameMapping m_nameMapping;

	private HAPDefinitionGroupDataAssociation m_outputMapping;
	
	private HAPDefinitionGroupDataAssociation m_inputMapping;
	
	//event handlers
	private Set<HAPHandler> m_eventHandlers;
	
	public HAPEmbededComponent() {
		this.m_nameMapping = new HAPNameMapping();
		this.m_eventHandlers = new HashSet<HAPHandler>();
		this.m_outputMapping = new HAPDefinitionGroupDataAssociation();
		this.m_inputMapping = new HAPDefinitionGroupDataAssociation();
	}
	
	public void setNameMapping(HAPNameMapping nameMapping) {   if(nameMapping!=null)  this.m_nameMapping = nameMapping;  }
	@Override
	public HAPNameMapping getNameMapping() {    return this.m_nameMapping;   }

	@Override
	public Set<HAPHandler> getEventHandlers(){   return this.m_eventHandlers;   }
	@Override
	public void addEventHandler(HAPHandler eventHandler) {  this.m_eventHandlers.add(eventHandler);   }

	public HAPDefinitionGroupDataAssociation getInputMapping() {   return this.m_inputMapping;   }
	public void setInputMapping(HAPDefinitionGroupDataAssociation contextMapping) {   this.m_inputMapping = contextMapping;   }
	public void addInputMapping(String name, HAPDefinitionDataAssociation mapping) {   this.m_inputMapping.addDataAssociation(name, mapping);  }

	public HAPDefinitionGroupDataAssociation getOutputMapping() {   return this.m_outputMapping;    }
	public void setOutputMapping(HAPDefinitionGroupDataAssociation contextMapping) {   this.m_outputMapping = contextMapping;   }
	public void addOutputMapping(String name, HAPDefinitionDataAssociation mapping) {   this.m_outputMapping.addDataAssociation(name, mapping);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, this.m_outputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_inputMapping.buildObject(jsonObj.optJSONArray(INPUTMAPPING), HAPSerializationFormat.JSON);
		this.m_outputMapping.buildObject(jsonObj.optJSONArray(OUTPUTMAPPING), HAPSerializationFormat.JSON);
		return true;
	}

	protected void cloneToEmbededComponent(HAPEmbededComponent embededComponent) {
		this.cloneToEntityInfo(embededComponent);
		for(HAPHandler eventHandler : this.m_eventHandlers) {
			embededComponent.addEventHandler(eventHandler.cloneHandler());
		}
		if(this.m_nameMapping==null)   embededComponent.m_nameMapping = null;
		else   embededComponent.m_nameMapping = this.m_nameMapping.cloneNameMapping();
		embededComponent.m_inputMapping = this.m_inputMapping.cloneGroupDataAssociation();
		embededComponent.m_outputMapping = this.m_outputMapping.cloneGroupDataAssociation();
	}
	
}
