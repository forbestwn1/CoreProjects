package com.nosliw.uiresource.page;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.uiresource.context.HAPContext;

//service definition which provide function for ui unit
@HAPEntityWithAttribute
public class HAPServiceDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String PARMS = "parms";

	private String m_name;
	
	private HAPContext m_parms;


	public String getName() {   return this.m_name;  }
	
	public HAPContext getParms() {  return this.m_parms;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(PARMS, this.m_parms.toStringValue(HAPSerializationFormat.JSON));
	}

}
