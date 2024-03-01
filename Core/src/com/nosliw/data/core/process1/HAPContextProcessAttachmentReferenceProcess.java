package com.nosliw.data.core.process1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPContextProcessAttachmentReferenceProcess extends HAPContextProcessor{

	public HAPContextProcessAttachmentReferenceProcess(HAPManualEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, complexEntity, runtimeEnv);
	}

	@Override
	protected Object processEntityAttachment(String attachmentName, Object entity) {
		return null;
	}

}
