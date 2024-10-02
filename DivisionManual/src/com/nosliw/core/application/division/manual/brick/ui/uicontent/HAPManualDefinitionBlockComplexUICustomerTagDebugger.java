package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTagDebugger;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.uitag.HAPUITagDefinition;

public class HAPManualDefinitionBlockComplexUICustomerTagDebugger extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockComplexUICustomerTagDebugger() {
		super(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100);
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTagDebugger.ATTRIBUTE, new LinkedHashMap<String, String>());
	}

	public Map<String, String> getTagAttributes(){    return (Map<String, String>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTagDebugger.ATTRIBUTE);  }
	public void setTagAttribute(String name, String value) {    this.getTagAttributes().put(name, value);   } 
	
	public String getUITagId() {    return (String)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTagDebugger.UITAGID);         }
	public void setUITagId(String uiTagId) {    this.setAttributeValueWithValue(HAPBlockComplexUICustomerTagDebugger.UITAGID, uiTagId);       }

	public HAPUITagDefinition getUITagDefinition() {    return (HAPUITagDefinition)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTagDebugger.TAGDEFINITION);      }
	public void setUITagDefinition(HAPUITagDefinition tagDef) {    this.setAttributeValueWithValue(HAPBlockComplexUICustomerTagDebugger.TAGDEFINITION, tagDef);     }
	
	@Override
	public Map<String, HAPDefinitionConstant> getConstantDefinitions(){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		out.putAll(super.getConstantDefinitions());
		out.putAll(HAPUtilityUITag.getConstantDefinitions(this.getUITagDefinition(), this.getTagAttributes()));
		return out;
	}
}
