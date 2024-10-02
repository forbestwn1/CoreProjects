package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTag;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.uitag.HAPUITagAttributeDefinition;
import com.nosliw.core.application.uitag.HAPUITagDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockComplexUICustomerTag extends HAPManualDefinitionBlockComplxWithUIContent{

	public static final String PARENTRELATIONS = "parentRelations";
	
	public HAPManualDefinitionBlockComplexUICustomerTag() {
		super(HAPEnumBrickType.UICUSTOMERTAG_100);
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTE, new LinkedHashMap<String, String>());
		this.setAttributeValueWithValue(PARENTRELATIONS, new ArrayList<HAPManualDefinitionBrickRelation>());
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION, new LinkedHashMap<String, HAPUITagAttributeDefinition>());
	}

	public HAPUITagDefinition getUITagDefinition() {    return (HAPUITagDefinition)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.TAGDEFINITION);      }
	public void setUITagDefinition(HAPUITagDefinition tagDef) {    this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.TAGDEFINITION, tagDef);     }
	
	public String getTagId() {   return (String)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.UITAGID);     }
	public void setTagId(String tagId) {   this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.UITAGID, tagId);       }

	public String getBase() {   return (String)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.BASE);     }
	public void setBase(String base) {   this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.BASE, base);       }
	
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.SCRIPTRESOURCEID);     }
	public void setScriptResourceId(HAPResourceId resourceId) {     this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.SCRIPTRESOURCEID, resourceId);         }

	public void addTagAttribute(String attrName, String attrValue) {     this.getTagAttributes().put(attrName, attrValue);        }
	public Map<String, String> getTagAttributes(){   return (Map<String, String>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.ATTRIBUTE);      }

	public void addTagAttributeDefinition(HAPUITagAttributeDefinition attrDef) {     this.getTagAttributeDefinitions().put(attrDef.getName(), attrDef);        }
	public Map<String, HAPUITagAttributeDefinition> getTagAttributeDefinitions(){   return (Map<String, HAPUITagAttributeDefinition>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION);      }

	public void addParentRelation(HAPManualDefinitionBrickRelation parentRelation) {    this.getParentRelations().add(parentRelation);      }
	public List<HAPManualDefinitionBrickRelation> getParentRelations(){	return (List<HAPManualDefinitionBrickRelation>)this.getAttributeValueOfValue(PARENTRELATIONS);	}
	
	@Override
	public Map<String, HAPDefinitionConstant> getConstantDefinitions(){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		out.putAll(super.getConstantDefinitions());
		out.putAll(HAPUtilityUITag.getConstantDefinitions(this.getUITagDefinition(), this.getTagAttributes()));
		return out;
	}
}
