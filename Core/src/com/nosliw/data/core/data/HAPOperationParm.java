package com.nosliw.data.core.data;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;

@HAPEntityWithAttribute
public class HAPOperationParm extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String DATA = "data";
	
	private String m_name;
	
	private HAPData m_data;
	
	public HAPOperationParm(HAPData data){
		this.m_data = data;
	}

	public HAPOperationParm(String name, HAPData data){
		this.m_name = name;
		this.m_data = data;
	}

	public String getName(){   return this.m_name;   }
	
	public HAPData getData(){   return this.m_data;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, m_name);
		jsonMap.put(DATA, HAPManagerSerialize.getInstance().toStringValue(m_data, HAPSerializationFormat.JSON));
	}	

}
