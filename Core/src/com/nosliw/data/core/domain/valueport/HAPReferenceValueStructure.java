package com.nosliw.data.core.domain.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPReferenceValueStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String ID = "id";

	//refer to name of value structure
	private String m_name;
	
	//refer to unique value structure definition id
	private String m_id;

	public String getName() {    return this.m_name;    }
	
	public String getId() {    return this.m_id;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, m_name);
		jsonMap.put(ID, m_id);
	}
}
