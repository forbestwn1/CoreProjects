package com.nosliw.ui.entity.uitag;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;

public class HAPDefinitionEntityUITagDefinition extends HAPDefinitionEntityInDomain{

	public static final String ATTR_INFO = "info";
	
	public static final String PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	public static final String CHILDRELATIONCONFIGURE = "childRelationConfigure";

	public void setInfo(HAPEntityInfo entityInfo) {}
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	public HAPIdEntityInDomain getValueContextEntityId() {}
	public HAPDefinitionEntityValueContext getValueContextEntity(HAPContextParser parserContext) {}

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {}
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {}
	
	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {}
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {}
	
}
