package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPWithResourceDependency;

@HAPEntityWithAttribute
public abstract class HAPAdapter extends HAPEntityInfoImp implements HAPWithResourceDependency{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";
	
	public abstract HAPWrapperValue getValueWrapper();
	
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.getValueWrapper().toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}

}
