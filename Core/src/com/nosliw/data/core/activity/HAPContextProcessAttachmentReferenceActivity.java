package com.nosliw.data.core.activity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessAttachmentReference;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuiteImp;
import com.nosliw.data.core.expression.HAPUtilityExpressionComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessAttachmentReferenceActivity extends HAPContextProcessAttachmentReference{

	public HAPContextProcessAttachmentReferenceActivity(HAPDefinitionEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYSUITE, complexEntity, runtimeEnv);
	}

	@Override
	protected Object processEntityAttachment(String attachmentName, Object entity) {
		return null;
		HAPDefinitionExpressionSuiteImp suite = HAPUtilityExpressionComponent.buildActivitySuiteFromComponent(this.getComplexEntity(), this.getRuntimeEnvironment());
		return suite.getEntityElement(attachmentName);
	}

}
