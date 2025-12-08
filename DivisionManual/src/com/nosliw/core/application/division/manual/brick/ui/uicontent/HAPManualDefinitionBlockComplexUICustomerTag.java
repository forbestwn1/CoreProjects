package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTag;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinition;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinitionAttribute;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.HAPWithValueContext;

public class HAPManualDefinitionBlockComplexUICustomerTag extends HAPManualDefinitionBlockComplxWithUIContent{

	public static final String PARENTRELATIONS = "parentRelations";
	
	public HAPManualDefinitionBlockComplexUICustomerTag() {
		super(HAPEnumBrickType.UICUSTOMERTAG_100);
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTE, new LinkedHashMap<String, String>());
		this.setAttributeValueWithValue(PARENTRELATIONS, new ArrayList<HAPManualDefinitionBrickRelation>());
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION, new LinkedHashMap<String, HAPUITagDefinitionAttribute>());
		this.setAttributeValueWithValue(HAPBlockComplexUICustomerTag.METADATA, new LinkedHashMap<String, String>());
	}

	@Override
	public void init() {
		this.setAttributeValueWithBrick(HAPWithValueContext.VALUECONTEXT, this.getManualBrickManager().newBrickDefinition(HAPManualEnumBrickType.VALUECONTEXT_100));
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

	public void addTagAttributeDefinition(HAPUITagDefinitionAttribute attrDef) {     this.getTagAttributeDefinitions().put(attrDef.getName(), attrDef);        }
	public Map<String, HAPUITagDefinitionAttribute> getTagAttributeDefinitions(){   return (Map<String, HAPUITagDefinitionAttribute>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION);      }

	public void addParentRelation(HAPManualDefinitionBrickRelation parentRelation) {    this.getParentRelations().add(parentRelation);      }
	public List<HAPManualDefinitionBrickRelation> getParentRelations(){	return (List<HAPManualDefinitionBrickRelation>)this.getAttributeValueOfValue(PARENTRELATIONS);	}
	
	public void addMetaData(String key, String value) {    this.getMetaData().put(key, value);      }
	public Map<String, String> getMetaData(){   return (Map<String, String>)this.getAttributeValueOfValue(HAPBlockComplexUICustomerTag.METADATA);     }
	
	@Override
	public Map<String, HAPDefinitionConstant> getConstantDefinitions(){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		out.putAll(super.getConstantDefinitions());
		out.putAll(HAPManualUtilityUITag.buildConstantDefinitions(this.getUITagDefinition(), this.getTagAttributes()));
		return out;
	}
}
