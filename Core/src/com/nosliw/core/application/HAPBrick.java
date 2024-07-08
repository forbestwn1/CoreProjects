package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.valueport.HAPWithValuePort;
import com.nosliw.core.application.valuecontext.HAPValueContext;
import com.nosliw.data.core.resource.HAPWithResourceDependency;

@HAPEntityWithAttribute
public abstract class HAPBrick extends HAPSerializableImp implements HAPEntityOrReference, HAPWithValuePort, HAPWithValueContext, HAPWithResourceDependency{

	@HAPAttribute
	public final static String BRICKTYPE = "brickType"; 

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 


	public abstract HAPIdBrickType getBrickType();

	public abstract List<HAPAttributeInBrick> getAttributes();

	@Override
	public abstract HAPValueContext getValueContext();

	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);

		jsonMap.put(BRICKTYPE, this.getBrickType().toStringValue(HAPSerializationFormat.JSON));
		
		List<String> attrJsonList = new ArrayList<String>();
		for(HAPAttributeInBrick attr : this.getAttributes()) {
			attrJsonList.add(attr.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrJsonList.toArray(new String[0])));
		
		if(this.getValueContext()!=null) {
			jsonMap.put(VALUECONTEXT, this.getValueContext().toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		
	}
}
