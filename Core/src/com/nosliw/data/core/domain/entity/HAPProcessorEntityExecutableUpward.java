package com.nosliw.data.core.domain.entity;

import com.nosliw.common.path.HAPPath;

public interface HAPProcessorEntityExecutableUpward {
	
	boolean process(HAPExecutableEntity entity, HAPPath path, HAPContextProcessor processContext, Object object);

}
