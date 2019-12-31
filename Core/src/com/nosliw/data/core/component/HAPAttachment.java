package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfoWritable;

public interface HAPAttachment extends HAPEntityInfoWritable{

	String getType();
	
	@Override
	HAPAttachment clone();
}
