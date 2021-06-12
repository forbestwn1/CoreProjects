package com.nosliw.data.core.expression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessAttachmentReference;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessAttachmentReferenceExpression extends HAPContextProcessAttachmentReference{

	public HAPContextProcessAttachmentReferenceExpression(HAPDefinitionEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, complexEntity, runtimeEnv);
	}

	@Override
	protected Object processEntityAttachment(String attachmentName, Object entity) {
		HAPDefinitionExpressionSuiteImp suite = HAPUtilityExpressionComponent.buildExpressionSuiteFromComponent(this.getComplexEntity(), this.getRuntimeEnvironment());
		return suite.getEntityElement(attachmentName);
	}

}
