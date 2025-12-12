package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTag;
import com.nosliw.core.application.brick.ui.uicontent.HAPUIEventHandlerInfoCustom;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinition;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinitionAttribute;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualBlockComplexUICustomerTag extends HAPManualBrickImp implements HAPBlockComplexUICustomerTag{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTE, new LinkedHashMap<String, String>());
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION, new LinkedHashMap<String, HAPUITagDefinitionAttribute>());
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.METADATA, new LinkedHashMap<String, String>());
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.EVENT, new LinkedHashMap<String, HAPUIEventHandlerInfoCustom>());
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

	public void addMetaData(String key, String value) {    this.getMetaData().put(key, value);      }
	public Map<String, String> getMetaData(){   return (Map<String, String>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.METADATA);     }
	
	public Map<String, HAPUIEventHandlerInfoCustom> getEvents(){    return (Map<String, HAPUIEventHandlerInfoCustom>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.EVENT);       }
	public void addEvent(HAPUIEventHandlerInfoCustom event) {    this.getEvents().put(event.getEvent(), event);     }
	
	
	public String getBase() {   return (String)this.getAttributeValueOfValue(BASE);    }
	public void setBase(String base) {     this.setAttributeValueWithValue(BASE, base);      }
	
	public HAPResourceId getScriptResourceId(){    return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);      }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);      }

	public Map<String, HAPUITagDefinitionAttribute> getAttributeDefinitions(){   return (Map<String, HAPUITagDefinitionAttribute>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION);      }
	public void addAttributeDefinition(HAPUITagDefinitionAttribute attrDef) {    this.getAttributeDefinitions().put(attrDef.getName(), attrDef);     }

	
	
}
