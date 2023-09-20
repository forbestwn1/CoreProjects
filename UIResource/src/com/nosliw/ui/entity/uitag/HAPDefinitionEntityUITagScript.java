package com.nosliw.ui.entity.uitag;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;

public class HAPDefinitionEntityUITagScript extends HAPDefinitionEntityInDomain{

	public static final String ATTR_INFO = "info";
	
	public static final String PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	public static final String CHILDRELATIONCONFIGURE = "childRelationConfigure";

	public void setInfo(HAPEntityInfo entityInfo) {}
	
	public HAPIdEntityInDomain getValueContextEntityId() {    return this.getAttributeValueEntityId(HAPWithValueContext.VALUECONTEXT);         }
	public HAPDefinitionEntityValueContext getValueContextEntity(HAPContextParser parserContext) {    return (HAPDefinitionEntityValueContext)this.getAttributeValueEntity(HAPWithValueContext.VALUECONTEXT, parserContext);   }

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {    this.setAttributeValueObject(PARENTRELATIONCONFIGURE, parentRelationConfigure);      }
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(PARENTRELATIONCONFIGURE);   }
	
	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {   this.setAttributeValueObject(CHILDRELATIONCONFIGURE, childRelationConfigure);    }
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {    return (HAPConfigureParentRelationComplex)this.getAttributeValue(CHILDRELATIONCONFIGURE);     }
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityUITagScript out = new HAPDefinitionEntityUITagScript();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
