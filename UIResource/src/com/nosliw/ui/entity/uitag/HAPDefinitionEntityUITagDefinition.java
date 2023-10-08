package com.nosliw.ui.entity.uitag;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPDefinitionEntityUITagDefinition extends HAPDefinitionEntityInDomain{

	public static final String INFO = "info";
	
	public static final String ATTRIBUTEDEFINITION = "attribute";
	
	public static final String EVENTDEFINITION = "event";
	
	public static final String PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	public static final String CHILDRELATIONCONFIGURE = "childRelationConfigure";

	public static final String BASE = "base";

	public static final String SCRIPTRESOURCEID = "scriptResourceId";
	
	public void setInfo(HAPEntityInfo entityInfo) {   this.setAttributeValueObject(INFO, entityInfo);    }
	
	public void setAttributeDefinition(Map<String, HAPUITagAttributeDefinition> attributes) {   this.setAttributeValueObject(ATTRIBUTEDEFINITION, attributes);    }
	public Map<String, HAPUITagAttributeDefinition> getAttributeDefinition() {   return (Map<String, HAPUITagAttributeDefinition>)this.getAttributeValue(ATTRIBUTEDEFINITION);    }
	
	public Map<String, HAPUITagEventDefinition> getEventDefinition(){   return (Map<String, HAPUITagEventDefinition>)this.getAttributeValue(EVENTDEFINITION);    }
	public void setEventDefinition(Map<String, HAPUITagEventDefinition> events) {   this.setAttributeValueObject(EVENTDEFINITION, events);    }
	
	public void setBaseName(String baseName) {    this.setAttributeValueObject(BASE, baseName);     }
	public String getBaseName() {    return (String)this.getAttributeValue(BASE);     }
	
	public void setScriptResourceId(HAPResourceId resourceId) {     this.setAttributeValueObject(SCRIPTRESOURCEID, resourceId);         }
	public HAPResourceId getScriptResourceId() {    return (HAPResourceId)this.getAttributeValue(SCRIPTRESOURCEID);      }
	
	public HAPIdEntityInDomain getValueContextEntityId() {    return this.getAttributeValueEntityId(HAPWithValueContext.VALUECONTEXT);         }
	public HAPDefinitionEntityValueContext getValueContextEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityValueContext)this.getAttributeValueEntity(HAPWithValueContext.VALUECONTEXT, parserContext);   }

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {    this.setAttributeValueObject(PARENTRELATIONCONFIGURE, parentRelationConfigure);      }
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(PARENTRELATIONCONFIGURE);   }
	
	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {   this.setAttributeValueObject(CHILDRELATIONCONFIGURE, childRelationConfigure);    }
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(CHILDRELATIONCONFIGURE);     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityUITagDefinition out = new HAPDefinitionEntityUITagDefinition();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
