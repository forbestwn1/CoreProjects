package com.nosliw.core.application.uitag1;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;

public class HAPDefinitionEntityUITagDefinition extends HAPManualBrick{

	public static final String INFO = "info";
	
	public static final String ATTRIBUTEDEFINITION = "attribute";
	
	public static final String EVENTDEFINITION = "event";
	
	public static final String PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	public static final String CHILDRELATIONCONFIGURE = "childRelationConfigure";

	public static final String BASE = "base";

	public static final String SCRIPTRESOURCEID = "scriptResourceId";
	
	public void setInfo(HAPEntityInfo entityInfo) {   this.setAttributeValueObject(INFO, entityInfo);    }
	
	public void setAttributeDefinition(Map<String, HAPUITagAttributeDefinition> attributes) {   this.setAttributeValueObject(ATTRIBUTEDEFINITION, attributes);    }
	public Map<String, HAPUITagAttributeDefinition> getAttributeDefinition() {   return (Map<String, HAPUITagAttributeDefinition>)this.getAttributeValueOfValue(ATTRIBUTEDEFINITION);    }
	
	public Map<String, HAPUITagEventDefinition> getEventDefinition(){   return (Map<String, HAPUITagEventDefinition>)this.getAttributeValueOfValue(EVENTDEFINITION);    }
	public void setEventDefinition(Map<String, HAPUITagEventDefinition> events) {   this.setAttributeValueObject(EVENTDEFINITION, events);    }
	
	public void setBaseName(String baseName) {    this.setAttributeValueObject(BASE, baseName);     }
	public String getBaseName() {    return (String)this.getAttributeValueOfValue(BASE);     }
	
	public void setScriptResourceId(HAPResourceId resourceId) {     this.setAttributeValueObject(SCRIPTRESOURCEID, resourceId);         }
	public HAPResourceId getScriptResourceId() {    return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);      }
	
	public HAPIdEntityInDomain getValueContextEntityId() {    return this.getAttributeValueEntityId(HAPWithValueContext.VALUECONTEXT);         }
	public HAPManualBrickValueContext getValueContextEntity(HAPContextParser parserContext) {    return (HAPManualBrickValueContext)this.getAttributeValueEntity(HAPWithValueContext.VALUECONTEXT, parserContext);   }

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {    this.setAttributeValueObject(PARENTRELATIONCONFIGURE, parentRelationConfigure);      }
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValueOfValue(PARENTRELATIONCONFIGURE);   }
	
	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {   this.setAttributeValueObject(CHILDRELATIONCONFIGURE, childRelationConfigure);    }
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValueOfValue(CHILDRELATIONCONFIGURE);     }
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityUITagDefinition out = new HAPDefinitionEntityUITagDefinition();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
