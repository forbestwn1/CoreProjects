package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.resource.HAPWithResourceDependency;

@HAPEntityWithAttribute
public abstract class HAPAttributeInBrick extends HAPEntityInfoImp implements HAPWithResourceDependency{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";

	@HAPAttribute
	public static final String ADAPTER = "adapter";

	
	public abstract HAPWrapperValue getValueWrapper();
	
	public abstract List<HAPAdapter> getAdapters();
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.getValueWrapper().toStringValue(HAPSerializationFormat.JAVASCRIPT));

		List<String> adapterJsonList = new ArrayList<String>();
		for(HAPAdapter adapter : this.getAdapters()) {
			adapterJsonList.add(adapter.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ADAPTER, HAPUtilityJson.buildArrayJson(adapterJsonList.toArray(new String[0])));
	}
	
}
