package com.nosliw.data.core.entity;

import com.nosliw.common.path.HAPPath;

public interface HAPProcessorEntityExecutableUpward {
	
	boolean process(HAPEntityExecutable entity, HAPPath path, Object object);

}
