package com.nosliw.data.core.activity.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivitySuite;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentImp;

//suite that contain multiple process
@HAPEntityWithAttribute
public class HAPResourceDefinitionActivitySuite extends HAPComponentImp implements HAPDefinitionActivitySuite{

	private Map<String, HAPDefinitionActivity> m_activities;
	
	public HAPResourceDefinitionActivitySuite() {
		this.m_activities = new LinkedHashMap<String, HAPDefinitionActivity>();
	}

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYSUITE;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	

	@Override
	public Set<HAPDefinitionActivity> getEntityElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionActivity getEntityElement(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEntityElement(HAPDefinitionActivity entityElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPComponent cloneComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionActivitySuite cloneActivitySuiteDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

}
