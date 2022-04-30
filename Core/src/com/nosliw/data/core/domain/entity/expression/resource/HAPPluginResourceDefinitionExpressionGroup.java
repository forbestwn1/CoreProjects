package com.nosliw.data.core.domain.entity.expression.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionResource;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.HAPDefinitionEntityExpressionSuite;
import com.nosliw.data.core.domain.entity.expression.HAPIdExpressionGroup;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPPluginResourceDefinitionExpressionGroup implements HAPPluginResourceDefinition{

	private HAPManagerResourceDefinition m_resourceDefMan;
	
	public HAPPluginResourceDefinitionExpressionGroup(HAPManagerResourceDefinition resourceDefMan) {
		this.m_resourceDefMan = resourceDefMan;
	}
	
	@Override
	public String getResourceType() {	return HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION;	}

	@Override
	public HAPIdEntityInDomain getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainEntityDefinitionResource entityDomain) {
		HAPResourceIdExpression expressionResourceId = new HAPResourceIdExpression(resourceId);
		HAPResourceIdSimple expressionSuiteResourceId = expressionResourceId.getExpressionSuiteResourceId();
		
		//get suite resource first
		HAPResourceDefinition expressionSuiteResourceDef = this.m_resourceDefMan.getResourceDefinition(expressionSuiteResourceId, entityDomain, null);
		
		//get group entity id
		HAPIdEntityInDomain groupEntityId = createGroupThroughSuite(expressionSuiteResourceDef.getEntityId(), expressionResourceId.getExpressionId().getExpressionGroupId(), entityDomain);
		return groupEntityId;
	}

	@Override
	public HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal resourceId, HAPPathLocationBase localRefBase, HAPDomainEntityDefinitionResource entityDomain) {
		HAPIdExpressionGroup expressionGroupId = new HAPIdExpressionGroup(resourceId.getName());
		
		HAPResourceIdLocal suiteResourceId = new HAPResourceIdLocal(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE);
		suiteResourceId.setName(expressionGroupId.getSuiteId());
		suiteResourceId.setSupplement(resourceId.getSupplement());
		
		HAPResourceDefinition suiteResourceDefinition = this.m_resourceDefMan.getResourceDefinition(suiteResourceId, entityDomain, localRefBase);
		
		HAPIdEntityInDomain groupEntityId = createGroupThroughSuite(suiteResourceDefinition.getEntityId(), expressionGroupId.getExpressionGroupId(), entityDomain);
		return groupEntityId;
	}
	
	private HAPIdEntityInDomain createGroupThroughSuite(HAPIdEntityInDomain suitEntityId, String groupName, HAPDomainEntityDefinitionResource entityDomain) {
		HAPDefinitionEntityExpressionSuite suite = (HAPDefinitionEntityExpressionSuite)entityDomain.getEntityInfoDefinition(suitEntityId).getEntity();
		return suite.getExpressionGroup(groupName);
	}

}
