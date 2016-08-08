package com.nosliw.entity.data;

import com.nosliw.common.exception.HAPServiceData;

public interface HAPDataWraperTask {

	public HAPServiceData process(HAPDataWraper wraper, Object data);
	
	public Object afterProcess(HAPDataWraper wraper, Object data);
	
}
