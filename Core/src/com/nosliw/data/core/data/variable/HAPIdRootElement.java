package com.nosliw.data.core.data.variable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPIdRootElement extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUESTRUCTUREID = "valueStructureId";
	
	@HAPAttribute
	public static final String ROOTNAME = "rootName";
	
	private String m_valueStructureId;
	
	private String m_rootName;
	
	public HAPIdRootElement(String valueStructureId, String rootName) {
		this.m_valueStructureId = valueStructureId;
		this.m_rootName = rootName;
	}
	
	public String getValueStructureId() {    return this.m_valueStructureId;     }
	
	public String getRootName() {    return this.m_rootName;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTUREID, this.m_valueStructureId);
		jsonMap.put(ROOTNAME, this.m_rootName);
	}
}
