package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPUtilityJson;

@HAPEntityWithAttribute
public class HAPValuePort extends HAPEntityInfoImp{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String IODIRECTION = "ioDirection";

	private List<String> m_valueStructures;

	private String m_type;
	
	private String m_ioDirection; 

	public HAPValuePort(String type, String ioDirection) {
		this.m_type = type;
		this.m_ioDirection = ioDirection;
		this.m_valueStructures = new ArrayList<String>();
	}
	
	public List<String> getValueStructureIds(){    return this.m_valueStructures;     }
	public void addValueStructureId(String id) {    this.m_valueStructures.add(id);    }

	public String getType() {     return this.m_type;    }

	public String getIODirection() {     return this.m_ioDirection;     }
	public void setIODirection(String ioDirection) {   this.m_ioDirection = ioDirection;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(IODIRECTION, this.getIODirection());
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(this.m_valueStructures.toArray(new String[0])));
	}
}
