package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.m.HAPManualBrick;

public interface HAPProcessorEntityExecutableUpward {
	
	boolean process(HAPManualBrick entity, HAPPath path, Object object);

}
