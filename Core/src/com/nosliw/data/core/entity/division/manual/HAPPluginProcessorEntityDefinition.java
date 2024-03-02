package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.entity.HAPEntityBundle;

public interface HAPPluginProcessorEntityDefinition {

	HAPEntityBundle process(HAPManualInfoEntity entityDef);
	
}
