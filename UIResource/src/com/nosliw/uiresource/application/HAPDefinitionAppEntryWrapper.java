package com.nosliw.uiresource.application;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponentContainerElement;

public class HAPDefinitionAppEntryWrapper extends HAPComponentContainerElement{
  
	public HAPDefinitionAppEntryWrapper(HAPDefinitionApp app, String entry) {
		super(app, entry);
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY;  }
	
	public HAPDefinitionApp getAppDefinition() {   return (HAPDefinitionApp)this.getContainer();   }
	
	public HAPDefinitionAppEntryUI getEntry() {   return (HAPDefinitionAppEntryUI)this.getElement();   }
	
}
