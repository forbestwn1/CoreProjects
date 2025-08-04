package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.division.manual.HAPManualBrickWithEntityInfo;
import com.nosliw.core.application.uitag.HAPUITagDefinition;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPBlockComplexUICustomerTagDebugger;

public class HAPManualBlockComplexUICustomerTagDebugger extends HAPManualBrickWithEntityInfo implements HAPBlockComplexUICustomerTagDebugger{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTagDebugger.ATTRIBUTE, new LinkedHashMap<String, String>());
	}
	
	public Map<String, String> getTagAttributes(){    return (Map<String, String>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTagDebugger.ATTRIBUTE);  }
	public void setTagAttribute(String name, String value) {    this.getTagAttributes().put(name, value);   } 
	
	public String getUITagId() {    return (String)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTagDebugger.UITAGID);         }
	public void setUITagId(String uiTagId) {    this.setAttributeValueWithValue(HAPBlockComplexUICustomerTagDebugger.UITAGID, uiTagId);       }

	public HAPUITagDefinition getUITagDefinition() {    return (HAPUITagDefinition)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTagDebugger.TAGDEFINITION);      }
	public void setUITagDefinition(HAPUITagDefinition tagDef) {    this.setAttributeValueWithValue(HAPBlockComplexUICustomerTagDebugger.TAGDEFINITION, tagDef);     }
	
}
