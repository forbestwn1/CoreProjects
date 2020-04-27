package com.nosliw.data.core.template;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.HAPData;

public interface HAPBuilderResourceDefinition extends HAPEntityInfo{

	@Override
	String getId();
	
	HAPBuilderOutput build(Map<String, HAPData> parms);

}
