package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessAttachmentReference;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessAttachmentReferenceActivity extends HAPContextProcessAttachmentReference{

	public HAPContextProcessAttachmentReferenceActivity(HAPDefinitionEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYSUITE, complexEntity, runtimeEnv);
	}

	@Override
	protected Object processEntityAttachment(String attachmentName, Object entity) {
		return HAPParserActivity.parseActivitySuiteDefinition((JSONObject)entity, this.getComplexEntity(), this.getRuntimeEnvironment().getActivityManager().getPluginManager());
	}

}
