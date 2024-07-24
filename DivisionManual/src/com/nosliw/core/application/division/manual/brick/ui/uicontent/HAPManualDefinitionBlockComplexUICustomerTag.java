package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTag;

public class HAPManualDefinitionBlockComplexUICustomerTag extends HAPManualDefinitionBlockComplxWithUIContent{

	
	
	public HAPManualDefinitionBlockComplexUICustomerTag() {
		super(HAPEnumBrickType.UICUSTOMERTAG_100);
		this.setAttributeWithValueValue(HAPBlockComplexUICustomerTag.ATTRIBUTE, new LinkedHashMap<String, String>());
	}

	public void setTagId(String tagId) {}

	public void addTagAttribute(String attrName, String attrValue) {     this.getTagAttributes().put(attrName, attrValue);        }
	public Map<String, String> getTagAttributes(){   return (Map<String, String>)this.getAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTE);      }


}
