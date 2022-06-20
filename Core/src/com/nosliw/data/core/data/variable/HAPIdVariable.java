package com.nosliw.data.core.data.variable;

import java.util.Map;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPIdVariable extends HAPSerializableImp{

	public static final String VALUESTRUCTUREID = "valueStructureId";
	
	public static final String ELEMENTPATH = "elementPath";
	
	private String m_valueStructureId;
	
	private HAPComplexPath m_elementPath;
	
	public HAPIdVariable(String valueStructureId, String variableName) {
		this.m_valueStructureId = valueStructureId;
		this.m_elementPath = new HAPComplexPath(variableName);
	}
	
	public String getValueStructureId() {    return this.m_valueStructureId;     }
	
	public HAPComplexPath getElementPath() {    return this.m_elementPath;   }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTUREID, this.m_valueStructureId);
		jsonMap.put(ELEMENTPATH, this.m_elementPath.getFullName());
	}
	
}
