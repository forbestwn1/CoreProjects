package com.nosliw.ui.entity.uitag;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.ui.entity.uicontent.HAPDefinitionEntityComplexUITag;

public class HAPPluginEntityDefinitionInDomainUITagDefinition extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainUITagDefinition(String entityType,
			Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGDEFINITION, HAPDefinitionEntityUITagDefinition.class, runtimeEnv);
	}

	@Override
	public boolean isComplexEntity() {   return false;   }

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityUITagDefinition uiTagDefinition = (HAPDefinitionEntityUITagDefinition)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
		
		JSONObject jsonObj = this.convertToJsonObject(obj);
		this.parseSimpleEntityAttribute(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
		this.parseSimpleEntityAttribute(jsonObj, entityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
		
		HAPEntityInfoImp info = new HAPEntityInfoImp();
		info.buildObject(jsonObj.optJSONObject(HAPDefinitionEntityUITagDefinition.ATTR_INFO), HAPSerializationFormat.JSON);
		uiTagDefinition.setInfo(info);
		
		
		HAPConfigureParentRelationComplex parentRelationConfigure = new HAPConfigureParentRelationComplex(); 
		JSONObject parentRelationConfigureJson = jsonObj.optJSONObject(HAPDefinitionEntityComplexUITag.ATTR_PARENTRELATIONCONFIGURE);
		if(parentRelationConfigureJson!=null) {
			parentRelationConfigure.buildObject(parentRelationConfigureJson, HAPSerializationFormat.JSON);
		}
		uiTagDefinition.setParentRelationConfigure(parentRelationConfigure);
		
		HAPConfigureParentRelationComplex childRelationConfigure = new HAPConfigureParentRelationComplex(); 
		JSONObject childRelationConfigureJson = jsonObj.optJSONObject(HAPDefinitionEntityComplexUITag.ATTR_CHILDRELATIONCONFIGURE);
		if(childRelationConfigureJson!=null) {
			childRelationConfigure.buildObject(childRelationConfigureJson, HAPSerializationFormat.JSON);
		}
		uiTagDefinition.setChildRelationConfigure(childRelationConfigure);

		
	}

}