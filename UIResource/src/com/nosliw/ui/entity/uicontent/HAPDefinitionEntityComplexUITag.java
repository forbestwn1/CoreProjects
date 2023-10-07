package com.nosliw.ui.entity.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.ui.entity.uitag.HAPUITagAttributeDefinition;

public class HAPDefinitionEntityComplexUITag extends HAPDefinitionEntityComplexWithUIContent implements HAPWithUIId{

	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	
	public void setTagId(String tagName) {   this.setAttributeValueObject(HAPExecutableEntityComplexUITag.TAGID, tagName);      }
	public String getTagId() {    return (String)this.getAttributeValue(HAPExecutableEntityComplexUITag.TAGID);     }

	public void setScriptResourceId(HAPResourceId resourceId) {     this.setAttributeValueObject(HAPExecutableEntityComplexUITag.SCRIPTRESOURCEID, resourceId);         }
	public HAPResourceId getScriptResourceId() {    return (HAPResourceId)this.getAttributeValue(HAPExecutableEntityComplexUITag.SCRIPTRESOURCEID);      }

	public void setAttributeDefinition(Map<String, HAPUITagAttributeDefinition> attributes) {   this.setAttributeValueObject(HAPExecutableEntityComplexUITag.ATTRIBUTEDEFINITION, attributes);    }
	public Map<String, HAPUITagAttributeDefinition> getAttributeDefinition(){    return (Map<String, HAPUITagAttributeDefinition>)this.getAttributeValue(HAPExecutableEntityComplexUITag.ATTRIBUTEDEFINITION);       }
	
	public void setBaseName(String baseName) {    this.setAttributeValueObject(HAPExecutableEntityComplexUITag.BASE, baseName);     }
	public String getBaseName() {    return (String)this.getAttributeValue(HAPExecutableEntityComplexUITag.BASE);     }

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
