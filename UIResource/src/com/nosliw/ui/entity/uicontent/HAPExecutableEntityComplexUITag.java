package com.nosliw.ui.entity.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;

public class HAPExecutableEntityComplexUITag extends HAPExecutableEntityComplexWithUIContent implements HAPWithUIId{

	@HAPAttribute
	public static final String TAGNAME = "tagName";
	
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	@HAPAttribute
	public static final String PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	@HAPAttribute
	public static final String CHILDRELATIONCONFIGURE = "childRelationConfigure";


	
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String SCRIPT = "script";
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";
	@HAPAttribute
	public static final String VALUESTRUCTUREEXE = "valueStructureExe";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	public HAPExecutableEntityComplexUITag() {
		this.setAttributeValueObject(ATTRIBUTE, new LinkedHashMap<String, String>());
	}
	
	public void setTagName(String tagName) {}

	@Override
	public String getUIId() {    return (String)this.getAttributeValue(HAPWithUIId.UIID);  }
	public void setUIId(String uiId) {    this.setAttributeValueObject(HAPWithUIId.UIID, uiId);    }

	public void addTagAttribute(String attrName, String attrValue) {     this.getTagAttributes().put(attrName, attrValue);        }
	public Map<String, String> getTagAttributes(){   return (Map<String, String>)this.getAttributeValue(HAPExecutableEntityComplexUITag.ATTRIBUTE);      }

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {   this.setAttributeValueObject(HAPExecutableEntityComplexUITag.PARENTRELATIONCONFIGURE, parentRelationConfigure);    }
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(HAPExecutableEntityComplexUITag.PARENTRELATIONCONFIGURE);   }

	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {this.setAttributeValueObject(HAPExecutableEntityComplexUITag.CHILDRELATIONCONFIGURE, childRelationConfigure);    }
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(HAPExecutableEntityComplexUITag.CHILDRELATIONCONFIGURE);   }

}
