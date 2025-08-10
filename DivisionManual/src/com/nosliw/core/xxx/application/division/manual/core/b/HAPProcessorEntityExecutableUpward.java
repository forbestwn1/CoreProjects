package com.nosliw.core.xxx.application.division.manual.core.b;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;

public interface HAPProcessorEntityExecutableUpward {
	
	boolean process(HAPManualBrick entity, HAPPath path, Object object);

}
