package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.attachment.HAPInfoAttachment;
import com.nosliw.data.core.complex.attachment.HAPProcessorAttachmentEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorAttachmentEntityExpression implements HAPProcessorAttachmentEntity{

	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPProcessorAttachmentEntityExpression(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityComplex complexEntity) {
		HAPDefinitionExpressionSuite suite = HAPUtilityExpressionComponent.buildExpressiionSuiteFromComponent(complexEntity, this.m_runtimeEnv);
		return suite.getEntityElement(attachmentInfo.getName());
	}

}
