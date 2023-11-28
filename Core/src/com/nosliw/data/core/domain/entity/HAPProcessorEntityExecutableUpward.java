package com.nosliw.data.core.domain.entity;

import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.component.HAPContextProcessor;

public interface HAPProcessorEntityExecutableUpward {
	
	boolean process(HAPExecutableEntity entity, HAPPath path, HAPContextProcessor processContext, Object object);

}
