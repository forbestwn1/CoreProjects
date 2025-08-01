package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.core.application.uitag.HAPUITagAttributeDefinition;
import com.nosliw.core.application.uitag.HAPUITagDefinition;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPBlockComplexUICustomerTag;

public class HAPManualBlockComplexUICustomerTag extends HAPManualBrickImp implements HAPBlockComplexUICustomerTag{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTE, new LinkedHashMap<String, String>());
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION, new LinkedHashMap<String, HAPUITagAttributeDefinition>());
	}
	
	public String getUITagId() {   return (String)this.getAttributeValueOfValue(UITAGID);  }
	public void setUITagId(String uiTagId) {    this.setAttributeValueWithValue(UITAGID, uiTagId);      }

	@Override
	public String getUIId() {   return (String)this.getAttributeValueOfValue(UIID);  }
	public void setUIId(String uiId) {    this.setAttributeValueWithValue(UIID, uiId);      }

	public void addTagAttribute(String attrName, String attrValue) {     this.getTagAttributes().put(attrName, attrValue);        }
	public Map<String, String> getTagAttributes(){   return (Map<String, String>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.ATTRIBUTE);      }

	public HAPUITagDefinition getUITagDefinition() {    return (HAPUITagDefinition)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.TAGDEFINITION);      }
	public void setUITagDefinition(HAPUITagDefinition tagDef) {    this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.TAGDEFINITION, tagDef);     }
	
	
	public String getBase() {   return (String)this.getAttributeValueOfValue(BASE);    }
	public void setBase(String base) {     this.setAttributeValueWithValue(BASE, base);      }
	
	public HAPResourceId getScriptResourceId(){    return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);      }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);      }

	public Map<String, HAPUITagAttributeDefinition> getAttributeDefinitions(){   return (Map<String, HAPUITagAttributeDefinition>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION);      }
	public void addAttributeDefinition(HAPUITagAttributeDefinition attrDef) {    this.getAttributeDefinitions().put(attrDef.getName(), attrDef);     }

}
