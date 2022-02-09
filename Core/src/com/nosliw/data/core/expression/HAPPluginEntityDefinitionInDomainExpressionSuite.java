package com.nosliw.data.core.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionSuite extends HAPPluginEntityDefinitionInDomainComplex{

	public HAPPluginEntityDefinitionInDomainExpressionSuite(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionExpressionSuite.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,
			HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionExpressionSuite suiteEntity = (HAPDefinitionExpressionSuite)definitionDomain.getComplexEntityInfo(entityId).getComplexEntity();
		
		//parse element
		JSONArray eleArray = jsonObj.getJSONArray(HAPDefinitionExpressionSuite.ELEMENT);
		for(int i=0; i<eleArray.length(); i++){
			//new element entity
			JSONObject eleObjJson = eleArray.getJSONObject(i);
			HAPIdEntityInDomain eleEntityId = HAPUtilityParserEntity.parseEntity(eleObjJson, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.getRuntimeEnvironment().getDomainEntityManager());
			suiteEntity.addExpressionGroup(HAPUtilityDomain.newInfoContainerElementSet(eleEntityId, eleObjJson));
			
			//build parent relation for complex child
			HAPInfoDefinitionEntityInDomainComplex entityInfo = definitionDomain.getComplexEntityInfo(eleEntityId);
			entityInfo.setParentId(entityId);
			entityInfo.setParentRelationConfigure(HAPUtilityDomain.createDefaultParentRelationConfigure());
		}
	}

}