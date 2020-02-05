package com.nosliw.uiresource.application;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.uiresource.resource.HAPResourceIdUIAppEntry;

public class HAPResourceDefinitionPluginAppEntry implements HAPPluginResourceDefinition{

	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public HAPResourceDefinitionPluginAppEntry(HAPManagerResourceDefinition resourceDefMan) {
		this.m_resourceDefMan = resourceDefMan;
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		HAPResourceIdUIAppEntry appEntryResourceId = new HAPResourceIdUIAppEntry(resourceId);
		HAPResourceIdSimple appResourceId = appEntryResourceId.getUIAppResourceId();
		HAPDefinitionApp appDef = (HAPDefinitionApp)this.m_resourceDefMan.getResourceDefinition(appResourceId);
		return new HAPDefinitionAppEntryWrapper(appDef, appEntryResourceId.getUIAppEntryId().getEntry());
	}
}