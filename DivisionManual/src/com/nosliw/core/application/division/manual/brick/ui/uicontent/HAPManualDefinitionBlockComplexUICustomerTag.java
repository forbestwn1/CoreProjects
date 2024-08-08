package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTag;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.uitag.HAPUITagAttributeDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockComplexUICustomerTag extends HAPManualDefinitionBlockComplxWithUIContent{

	public static final String PARENTRELATIONS = "parentRelations";
	
	public HAPManualDefinitionBlockComplexUICustomerTag() {
		super(HAPEnumBrickType.UICUSTOMERTAG_100);
		this.setAttributeWithValueValue(HAPBlockComplexUICustomerTag.ATTRIBUTE, new LinkedHashMap<String, String>());
		this.setAttributeWithValueValue(PARENTRELATIONS, new ArrayList<HAPManualDefinitionBrickRelation>());
		this.setAttributeWithValueValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION, new LinkedHashMap<String, HAPUITagAttributeDefinition>());
	}

	public String getTagId() {   return (String)this.getAttributeValueWithValue(HAPBlockComplexUICustomerTag.UITAGID);     }
	public void setTagId(String tagId) {   this.setAttributeWithValueValue(HAPBlockComplexUICustomerTag.UITAGID, tagId);       }

	public String getBase() {   return (String)this.getAttributeValueWithValue(HAPBlockComplexUICustomerTag.BASE);     }
	public void setBase(String base) {   this.setAttributeWithValueValue(HAPBlockComplexUICustomerTag.BASE, base);       }
	
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueWithValue(HAPBlockComplexUICustomerTag.SCRIPTRESOURCEID);     }
	public void setScriptResourceId(HAPResourceId resourceId) {     this.setAttributeWithValueValue(HAPBlockComplexUICustomerTag.SCRIPTRESOURCEID, resourceId);         }

	public void addTagAttribute(String attrName, String attrValue) {     this.getTagAttributes().put(attrName, attrValue);        }
	public Map<String, String> getTagAttributes(){   return (Map<String, String>)this.getAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTE);      }

	public void addTagAttributeDefinition(HAPUITagAttributeDefinition attrDef) {     this.getTagAttributeDefinitions().put(attrDef.getName(), attrDef);        }
	public Map<String, HAPUITagAttributeDefinition> getTagAttributeDefinitions(){   return (Map<String, HAPUITagAttributeDefinition>)this.getAttributeValueWithValue(HAPBlockComplexUICustomerTag.ATTRIBUTEDEFINITION);      }

	public void addParentRelation(HAPManualDefinitionBrickRelation parentRelation) {    this.getParentRelations().add(parentRelation);      }
	public List<HAPManualDefinitionBrickRelation> getParentRelations(){	return (List<HAPManualDefinitionBrickRelation>)this.getAttributeValueWithValue(PARENTRELATIONS);	}
	
	@Override
	public Map<String, HAPDefinitionConstant> getConstantDefinitions(){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		out.putAll(super.getConstantDefinitions());
		
		Map<String, String> attrs = this.getTagAttributes();
		for(String attrName : attrs.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(attrName, attrs.get(attrName));
			out.put(HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE + constantDef.getName(), constantDef);
		}
		return out;
	}
}
