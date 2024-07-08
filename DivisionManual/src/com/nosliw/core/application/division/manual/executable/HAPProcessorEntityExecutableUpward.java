package com.nosliw.core.application.division.manual.executable;

import com.nosliw.common.path.HAPPath;

public interface HAPProcessorEntityExecutableUpward {
	
	boolean process(HAPManualBrick entity, HAPPath path, Object object);

}
