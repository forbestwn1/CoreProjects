package com.nosliw.uiresource.application;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponentContainerElement;

public class HAPDefinitionAppEntry extends HAPComponentContainerElement{
  
	public HAPDefinitionAppEntry(HAPDefinitionApp app, String entry) {
		super(app, entry);
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY;  }
	
	public HAPDefinitionApp getAppDefinition() {   return (HAPDefinitionApp)this.getContainer();   }
	
	public HAPDefinitionAppElementUI getEntry() {   return (HAPDefinitionAppElementUI)this.getElement();   }
	
}
