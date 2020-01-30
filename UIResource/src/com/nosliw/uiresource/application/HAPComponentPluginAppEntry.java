package com.nosliw.uiresource.application;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPManagerComponent;
import com.nosliw.data.core.component.HAPPluginComponent;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.uiresource.resource.HAPResourceIdUIAppEntry;

public class HAPComponentPluginAppEntry implements HAPPluginComponent{

	private HAPManagerComponent m_componentMan;
	
	public HAPComponentPluginAppEntry(HAPManagerComponent componentMan) {
		this.m_componentMan = componentMan;
	}
	
	@Override
	public String getComponentType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY;  }

	@Override
	public HAPComponent getComponent(HAPResourceIdSimple resourceId) {
		HAPResourceIdUIAppEntry appEntryResourceId = new HAPResourceIdUIAppEntry(resourceId);
		HAPResourceIdSimple appResourceId = appEntryResourceId.getUIAppResourceId();
		HAPDefinitionApp appDef = (HAPDefinitionApp)this.m_componentMan.getComponent(appResourceId);
		return new HAPDefinitionAppEntryWrapper(appDef, appEntryResourceId.getUIAppEntryId().getEntry());
	}
}
