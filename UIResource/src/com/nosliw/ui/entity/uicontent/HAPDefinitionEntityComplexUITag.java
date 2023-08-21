package com.nosliw.ui.entity.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityComplexUITag extends HAPDefinitionEntityComplexWithUIContent{

	@HAPAttribute
	public static final String ATTR_UIID = "uiUid";
	
	@HAPAttribute
	public static final String ATTR_TAGNAME = "tagName";
	
	@HAPAttribute
	public static final String ATTR_PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	@HAPAttribute
	public static final String ATTR_CHILDRELATIONCONFIGURE = "childRelationConfigure";

	@HAPAttribute
	public static final String ATTR_ATTRIBUTE = "attribute";

	
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	public void setTagName(String tagName) {   this.setAttributeValueObject(ATTR_TAGNAME, tagName);      }

	public void setUIId(String uiId) {    this.setAttributeValueObject(ATTR_UIID, uiId);    }

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {   this.setAttributeValueObject(ATTR_PARENTRELATIONCONFIGURE, parentRelationConfigure);    }
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(ATTR_PARENTRELATIONCONFIGURE);   }

	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {this.setAttributeValueObject(ATTR_CHILDRELATIONCONFIGURE, childRelationConfigure);    }
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(ATTR_CHILDRELATIONCONFIGURE);   }

	public void addAttribute(String attrName, String attrValue) {     ((Map<String, String>)this.getAttributeValue(ATTR_ATTRIBUTE)).put(attrName, attrValue);        }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUITag out = new HAPDefinitionEntityComplexUITag();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
