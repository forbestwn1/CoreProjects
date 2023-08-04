package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
abstract public class HAPExecutableSegmentExpression extends HAPExecutableImp{
	
	@HAPAttribute
	public static String TYPE = "type";

	public abstract String getType();

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}

	
}
