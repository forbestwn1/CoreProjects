package com.nosliw.data.core.component.valuestructure;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessAttachmentReference;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPParserStructure;

public class HAPContextProcessAttachmentReferenceValueStructure extends HAPContextProcessAttachmentReference{

	public HAPContextProcessAttachmentReferenceValueStructure(HAPDefinitionEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, complexEntity, runtimeEnv);
	}

	@Override
	protected Object processEntityAttachment(String attachmentName, Object entity) {
		return HAPParserStructure.parseRoots(entity);
	}

}
