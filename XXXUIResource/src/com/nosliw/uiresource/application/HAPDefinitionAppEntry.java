package com.nosliw.uiresource.application;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.component.HAPDefinitionEntityElementInContainerComponent;

public class HAPDefinitionAppEntry extends HAPDefinitionEntityElementInContainerComponent{
  
	private HAPDefinitionAppEntry() {}
	
	public HAPDefinitionAppEntry(HAPDefinitionApp app, String entry) {
		super(app, entry);
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPPENTRY;  }
	
	public HAPDefinitionApp getAppDefinition() {   return (HAPDefinitionApp)this.getResourceContainer();   }
	
	public HAPDefinitionAppElementUI getEntry() {   return (HAPDefinitionAppElementUI)this.getComponentEntity();   }

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {
		HAPDefinitionAppEntry out = new HAPDefinitionAppEntry();
		this.cloneToComponentContainerElement(out);
		return out;
	}

}
