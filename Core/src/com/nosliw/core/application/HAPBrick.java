package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.valueport.HAPWithValuePort;
import com.nosliw.data.core.resource.HAPWithResourceDependency;

@HAPEntityWithAttribute
public interface HAPBrick extends HAPSerializable, HAPEntityOrReference, HAPWithValuePort, HAPWithValueContext, HAPWithResourceDependency{

	@HAPAttribute
	public final static String BRICKTYPE = "brickType"; 

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 

	public abstract HAPIdBrickType getBrickType();

	public abstract List<HAPAttributeInBrick> getAttributes();


	public static void buildJSJsonMap(HAPBrick brick, Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(BRICKTYPE, brick.getBrickType().toStringValue(HAPSerializationFormat.JSON));
		
		List<String> attrJsonList = new ArrayList<String>();
		for(HAPAttributeInBrick attr : brick.getAttributes()) {
			attrJsonList.add(attr.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrJsonList.toArray(new String[0])));
		
		if(brick.getValueContext()!=null) {
			jsonMap.put(VALUECONTEXT, brick.getValueContext().toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
	}
}
