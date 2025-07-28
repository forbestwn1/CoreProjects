package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPManagerResourceDefinition;
import com.nosliw.core.resource.HAPPluginResourceDefinition;
import com.nosliw.core.resource.HAPResourceDefinition1;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.uiresource.application.HAPDefinitionApp;
import com.nosliw.uiresource.application.HAPDefinitionAppEntry;

public class HAPResourceDefinitionPluginAppEntry implements HAPPluginResourceDefinition{

	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public HAPResourceDefinitionPluginAppEntry(HAPManagerResourceDefinition resourceDefMan) {
		this.m_resourceDefMan = resourceDefMan;
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPPENTRY;  }

	@Override
	public HAPResourceDefinition1 getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId) {
		HAPResourceIdUIAppEntry appEntryResourceId = new HAPResourceIdUIAppEntry(resourceId);
		HAPResourceIdSimple appResourceId = appEntryResourceId.getUIAppResourceId();
		HAPDefinitionApp appDef = (HAPDefinitionApp)this.m_resourceDefMan.getResourceDefinition(appResourceId);
		return new HAPDefinitionAppEntry(appDef, appEntryResourceId.getUIAppEntryId().getEntry());
	}

	@Override
	public HAPResourceDefinition1 parseResourceEntity(Object content) {
		// TODO Auto-generated method stub
		return null;
	}
}
