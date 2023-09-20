package com.nosliw.ui.entity.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityComplexUITag extends HAPDefinitionEntityComplexWithUIContent implements HAPWithUIId{


	
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	public void setTagName(String tagName) {   this.setAttributeValueObject(HAPExecutableEntityComplexUITag.TAGNAME, tagName);      }
	public String getTagName() {    return (String)this.getAttributeValue(HAPExecutableEntityComplexUITag.TAGNAME);     }

	@Override
	public String getUIId() {    return (String)this.getAttributeValue(HAPWithUIId.UIID);  }
	public void setUIId(String uiId) {    this.setAttributeValueObject(HAPWithUIId.UIID, uiId);    }

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {   this.setAttributeValueObject(HAPExecutableEntityComplexUITag.PARENTRELATIONCONFIGURE, parentRelationConfigure);    }
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(HAPExecutableEntityComplexUITag.PARENTRELATIONCONFIGURE);   }

	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {this.setAttributeValueObject(HAPExecutableEntityComplexUITag.CHILDRELATIONCONFIGURE, childRelationConfigure);    }
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(HAPExecutableEntityComplexUITag.CHILDRELATIONCONFIGURE);   }

	public void addTagAttribute(String attrName, String attrValue) {     this.getTagAttributes().put(attrName, attrValue);        }
	public Map<String, String> getTagAttributes(){   return (Map<String, String>)this.getAttributeValue(HAPExecutableEntityComplexUITag.ATTRIBUTE);      }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityComplexUITag out = new HAPDefinitionEntityComplexUITag();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
