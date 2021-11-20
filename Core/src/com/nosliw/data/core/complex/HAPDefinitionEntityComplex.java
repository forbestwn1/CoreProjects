package com.nosliw.data.core.complex;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.resource.HAPEntityResourceDefinition;

//entity that have data definition and attachment, static information
public interface HAPDefinitionEntityComplex  extends HAPEntityResourceDefinition, HAPEntityInfo, HAPWithValueStructure, HAPWithAttachment{

	void cloneToComplexEntityDefinition(HAPDefinitionEntityComplex complexEntity, boolean cloneValueStructure);

	HAPDefinitionEntityComplex cloneComplexEntityDefinition();
}
