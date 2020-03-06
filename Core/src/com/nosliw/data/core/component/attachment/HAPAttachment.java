package com.nosliw.data.core.component.attachment;

import com.nosliw.common.info.HAPEntityInfoWritable;

public interface HAPAttachment extends HAPEntityInfoWritable{

	String getType();

	String getResourceType();
	
	HAPAttachment cloneAttachment();
}
