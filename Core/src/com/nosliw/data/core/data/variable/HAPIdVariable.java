package com.nosliw.data.core.data.variable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPIdVariable extends HAPSerializableImp{

	@HAPAttribute
	public static final String ROOTELEMENTID = "rootElementId";
	
	@HAPAttribute
	public static final String ELEMENTPATH = "elementPath";
	
	private HAPIdRootElement m_rootElementId;
	
	private HAPPath m_elementPath;
	
	public HAPIdVariable(String valueStructureId, String variableName) {
		HAPComplexPath complexPath = new HAPComplexPath(variableName);
		this.m_rootElementId = new HAPIdRootElement(valueStructureId, complexPath.getRoot());
		this.m_elementPath = complexPath.getPath();
	}
	
	public HAPIdRootElement getRootElementId() {    return this.m_rootElementId;     }
	
	public HAPPath getElementPath() {    return this.m_elementPath;   }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ROOTELEMENTID, this.m_rootElementId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ELEMENTPATH, this.m_elementPath.getPath());
	}
}
