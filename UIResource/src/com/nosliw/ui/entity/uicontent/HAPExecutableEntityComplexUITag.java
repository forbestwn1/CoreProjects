package com.nosliw.ui.entity.uicontent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.variable.HAPIdVariable;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.ui.entity.uitag.HAPUITagAttributeDefinition;

@HAPEntityWithAttribute
public class HAPExecutableEntityComplexUITag extends HAPExecutableEntityComplexWithUIContent implements HAPWithUIId{

	@HAPAttribute
	public static final String TAGID = "tagId";
	
	@HAPAttribute
	public static final String ATTRIBUTEDEFINITION = "attributeDefinition";

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";

	@HAPAttribute
	public static final String PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	@HAPAttribute
	public static final String CHILDRELATIONCONFIGURE = "childRelationConfigure";

	@HAPAttribute
	public static final String BASE = "base";

	@HAPAttribute
	public static final String VARIABLEBYNAME = "variableByName";

	@HAPAttribute
	public static final String SCRIPTRESOURCEID = "scriptResourceId";
	
	
	
	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String SCRIPT = "script";
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
		this.setAttributeValueObject(VARIABLEBYNAME, new LinkedHashMap<String, HAPIdVariable>());
	}
	
	public void setTagId(String tagId) {   this.setAttributeValueObject(TAGID, tagId);    }
	public String getTagId() {   return (String)this.getAttributeValue(TAGID);  }

	public void setAttributeDefinition(Map<String, HAPUITagAttributeDefinition> attributes) {   this.setAttributeValueObject(ATTRIBUTEDEFINITION, attributes);    }

	public void setBaseName(String baseName) {    this.setAttributeValueObject(BASE, baseName);     }
	public String getBaseName() {    return (String)this.getAttributeValue(BASE);     }
	
	public void setScriptResourceId(HAPResourceId resourceId) {     this.setAttributeValueObject(SCRIPTRESOURCEID, resourceId);         }
	public HAPResourceId getScriptResourceId() {    return (HAPResourceId)this.getAttributeValue(SCRIPTRESOURCEID);      }
	
	@Override
	public String getUIId() {    return (String)this.getAttributeValue(HAPWithUIId.UIID);  }
	public void setUIId(String uiId) {    this.setAttributeValueObject(HAPWithUIId.UIID, uiId);    }

	public void addTagAttribute(String attrName, String attrValue) {     this.getTagAttributes().put(attrName, attrValue);        }
	public Map<String, String> getTagAttributes(){   return (Map<String, String>)this.getAttributeValue(HAPExecutableEntityComplexUITag.ATTRIBUTE);      }

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {   this.setAttributeValueObject(HAPExecutableEntityComplexUITag.PARENTRELATIONCONFIGURE, parentRelationConfigure);    }
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(HAPExecutableEntityComplexUITag.PARENTRELATIONCONFIGURE);   }

	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {this.setAttributeValueObject(HAPExecutableEntityComplexUITag.CHILDRELATIONCONFIGURE, childRelationConfigure);    }
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(HAPExecutableEntityComplexUITag.CHILDRELATIONCONFIGURE);   }

	public Map<String, HAPIdVariable> getVariablesByName(){    return (Map<String, HAPIdVariable>)this.getAttributeValue(VARIABLEBYNAME);       }
	public void addVariableByName(String name, HAPIdVariable variableId) {    this.getVariablesByName().put(name, variableId);     }
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManager resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		dependency.add(new HAPResourceDependency(new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, this.getTagId())));
	}

}
