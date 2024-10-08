package com.nosliw.data.core.process1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessAttachmentReferenceProcess extends HAPContextProcessor{

	public HAPContextProcessAttachmentReferenceProcess(HAPManualBrickComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, complexEntity, runtimeEnv);
	}

	@Override
	protected Object processEntityAttachment(String attachmentName, Object entity) {
		return null;
	}

}
