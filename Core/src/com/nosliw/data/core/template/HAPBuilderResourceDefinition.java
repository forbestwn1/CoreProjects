package com.nosliw.data.core.template;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.data.core.HAPData;

public interface HAPBuilderResourceDefinition extends HAPEntityInfo{

	String getId();
	
	HAPResourceDefinition build(HAPContentTemplate templateContent, Map<String, HAPData> parms);

}
