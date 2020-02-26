package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public interface HAPResourceDefinitionComplex extends HAPEntityInfoWritable, HAPWithDataContext, HAPWithAttachment, HAPResourceDefinition{

	HAPChildrenComponentIdContainer getChildrenComponentId();

	void cloneToComplexEntity(HAPResourceDefinitionComplex complexEntity);
}
