package com.nosliw.data.core.entity.division.manual;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpComplex extends HAPPluginParserEntityImp{

	public HAPPluginParserEntityImpComplex(HAPIdEntityType entityTypeId, Class<? extends HAPManualEntity> entityClass,
			HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(entityTypeId, entityClass, manualDivisionEntityMan, runtimeEnv);
	}


	@Override
	protected void parseDefinitionContentJson(HAPManualEntity entityDefinition, Object jsonValue, HAPContextParse parseContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
		
		this.parseComplexDefinitionContentJson(entityId, jsonObj, parserContext);
	}

	protected void parseComplexDefinitionContentJson(HAPManualEntity entityDefinition, JSONObject jsonObj, HAPContextParse parseContext) {}

}