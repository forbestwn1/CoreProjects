package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPProcessorAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment1.HAPInfoAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorAttachmentEntityExpression implements HAPProcessorAttachmentEntity{

	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPProcessorAttachmentEntityExpression(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityInDomainComplex complexEntity) {
		HAPDefinitionEntityExpressionSuite suite = HAPUtilityExpressionComponent.buildExpressiionSuiteFromComponent(complexEntity, this.m_runtimeEnv);
		return suite.getEntityElement(attachmentInfo.getName());
	}

}
