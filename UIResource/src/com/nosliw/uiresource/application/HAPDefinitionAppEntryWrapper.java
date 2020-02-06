package com.nosliw.uiresource.application;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPAttachment;
import com.nosliw.data.core.component.HAPAttachmentContainer;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPHandlerEvent;
import com.nosliw.data.core.component.HAPHandlerLifecycle;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPDefinitionAppEntryWrapper extends HAPSerializableImp implements HAPComponent, HAPResourceDefinition{

	@HAPAttribute
	public static String APP = "app";

	@HAPAttribute
	public static String ENTRY = "entry";

	private HAPResourceId m_resourceId;

	private HAPDefinitionApp m_app;
	
	private String m_entry;

	public HAPDefinitionAppEntryWrapper(HAPDefinitionApp app, String entry) {
		this.m_app = app;
		this.m_entry = entry;
	}
	
	public HAPDefinitionApp getAppDefinition() {   return this.m_app;   }
	public String getEntry() {   return this.m_entry;   }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		return this.m_app.getEntry(this.m_entry).getChildrenComponentId();
	}
	
	@Override
	public HAPInfo getInfo() {
		return this.m_app.getEntry(m_entry).getInfo();
	}

	
	@Override
	public void setResourceId(HAPResourceId resourceId) {  this.m_resourceId = resourceId; }

	@Override
	public HAPResourceId getResourceId() {  return this.m_resourceId;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTRY, this.m_entry);
		jsonMap.put(APP, HAPJsonUtility.buildJson(this.m_app, HAPSerializationFormat.JSON));
		jsonMap.put(HAPEntityInfo.INFO, HAPJsonUtility.buildJson(this.getInfo(), HAPSerializationFormat.JSON));
	}

	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPEntityInfo clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPAttachmentContainer getAttachmentContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, HAPAttachment> getAttachmentsByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mergeBy(HAPWithAttachment parent, String mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<HAPHandlerLifecycle> getLifecycleAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLifecycleAction(HAPHandlerLifecycle lifecycleAction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<HAPHandlerEvent> getEventHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventHandler(HAPHandlerEvent eventHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getComponentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPContextGroup getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(HAPContextGroup context) {
		// TODO Auto-generated method stub
		
	}

}
