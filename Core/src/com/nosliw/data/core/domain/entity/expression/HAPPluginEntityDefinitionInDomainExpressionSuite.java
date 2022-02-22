package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPConfigureEntityInDomainComplex;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionSuite extends HAPPluginEntityDefinitionInDomainComplex{

	public HAPPluginEntityDefinitionInDomainExpressionSuite(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityExpressionSuite.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,
			HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityExpressionSuite suiteEntity = (HAPDefinitionEntityExpressionSuite)definitionDomain.getComplexEntityInfo(entityId).getComplexEntity();
		
		//parse element
		
		Object groupObj = jsonObj.get(HAPDefinitionEntityExpressionSuite.GROUP);
		HAPConfigureEntityInDomainComplex groupEntityInfo = null; 
		if(groupObj instanceof JSONObject) {
			
		}
		
		JSONArray eleArray = jsonObj.getJSONArray(HAPDefinitionEntityExpressionSuite.GROUP);
		for(int i=0; i<eleArray.length(); i++){
			//new element entity
			JSONObject eleObjJson = eleArray.getJSONObject(i);
			
			HAPIdEntityInDomain eleEntityId = HAPUtilityParserEntity.parseEntity(eleObjJson, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, HAPUtilityDomain.getContextParse(entityId, definitionDomain), this.getRuntimeEnvironment().getDomainEntityManager());
			suiteEntity.addExpressionGroup(HAPUtilityDomain.newInfoContainerElementSet(eleEntityId, eleObjJson));
			
			//build parent relation for complex child
			HAPConfigureEntityInDomainComplex entityInfo = definitionDomain.getComplexEntityInfo(eleEntityId);
			entityInfo.setParentId(entityId);
			entityInfo.setParentRelationConfigure(HAPUtilityDomain.createDefaultParentRelationConfigure());
		}
	}

}
