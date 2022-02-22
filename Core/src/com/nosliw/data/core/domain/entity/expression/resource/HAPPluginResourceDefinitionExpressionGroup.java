package com.nosliw.data.core.domain.entity.expression.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.component.HAPLocalReferenceBase;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoComplexEntityDefinition;
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
	public HAPIdEntityInDomain getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainDefinitionEntity entityDomain) {
		HAPResourceIdExpression expressionResourceId = new HAPResourceIdExpression(resourceId);
		HAPResourceIdSimple expressionSuiteResourceId = expressionResourceId.getExpressionSuiteResourceId();
		
		//get suite resource first
		HAPResourceDefinition expressionSuiteResourceDef = this.m_resourceDefMan.getResourceDefinition(expressionSuiteResourceId, entityDomain, null);
		
		//get group entity id
		HAPIdEntityInDomain groupEntityId = createGroupThroughSuite(expressionSuiteResourceDef.getEntityId(), expressionResourceId.getExpressionId().getExpressionGroupId(), entityDomain);
		return groupEntityId;
	}

	@Override
	public HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal resourceId, HAPLocalReferenceBase localRefBase, HAPDomainDefinitionEntity entityDomain) {
		HAPIdExpressionGroup expressionGroupId = new HAPIdExpressionGroup(resourceId.getName());
		
		HAPResourceIdLocal suiteResourceId = new HAPResourceIdLocal(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE);
		suiteResourceId.setName(expressionGroupId.getSuiteId());
		suiteResourceId.setSupplement(resourceId.getSupplement());
		
		HAPResourceDefinition suiteResourceDefinition = this.m_resourceDefMan.getResourceDefinition(suiteResourceId, entityDomain, localRefBase);
		
		HAPIdEntityInDomain groupEntityId = createGroupThroughSuite(suiteResourceDefinition.getEntityId(), expressionGroupId.getExpressionGroupId(), entityDomain);
		return groupEntityId;
	}
	
	private HAPIdEntityInDomain createGroupThroughSuite(HAPIdEntityInDomain suitEntityId, String groupId, HAPDomainDefinitionEntity entityDomain) {
		HAPDefinitionEntityExpressionSuite suite = (HAPDefinitionEntityExpressionSuite)entityDomain.getEntityInfo(suitEntityId).getEntity();
		suite.get
		HAPElementContainerResourceDefinitionEntityExpressionSuite expressionGroup = suite.getContainerElement(groupId);
		HAPIdEntityInDomain groupEntityId = entityDomain.addComplexEntity(new HAPInfoComplexEntityDefinition(expressionGroup, new HAPConfigureParentRelationComplex()));
		return groupEntityId;
	}

}
