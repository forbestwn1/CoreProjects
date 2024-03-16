package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public interface HAPProcessorEntityExecutableUpward {
	
	boolean process(HAPBrick entity, HAPPath path, Object object);

}
