package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public interface HAPComplexResourceDefinition extends HAPEntityInfo, HAPWithDataContext, HAPWithAttachment, HAPResourceDefinition{

	HAPChildrenComponentIdContainer getChildrenComponentId();

}
