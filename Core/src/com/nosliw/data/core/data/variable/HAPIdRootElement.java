package com.nosliw.data.core.data.variable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPIdRootElement extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUESTRUCTUREID = "valueStructureId";
	
	@HAPAttribute
	public static final String ROOTNAME = "rootName";
	
	private String m_valueStructureId;
	
	private String m_rootName;
	
	public HAPIdRootElement(String reference) {
		String[] parts = HAPUtilityNamingConversion.splitTextByTwoPart(reference, HAPConstantShared.SEPERATOR_LEVEL1);
		this.m_rootName = parts[0];
		if(parts.length>1)   this.m_valueStructureId = parts[1];
	}
	
	public HAPIdRootElement(String valueStructureId, String rootName) {
		this.m_valueStructureId = valueStructureId;
		this.m_rootName = rootName;
	}
	
	public String getValueStructureId() {    return this.m_valueStructureId;     }
	
	public String getRootName() {    return this.m_rootName;   }

	public HAPComplexPath getPath() {    return new HAPComplexPath(this.m_valueStructureId, this.m_rootName);     }
	
	@Override
	protected String buildLiterate(){  
		return HAPUtilityNamingConversion.cascadeElements(new String[] {this.m_rootName, this.m_valueStructureId}, HAPConstantShared.SEPERATOR_LEVEL1); 
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTUREID, this.m_valueStructureId);
		jsonMap.put(ROOTNAME, this.m_rootName);
	}
	
	@Override
	public int hashCode() {		return this.buildLiterate().hashCode();	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HAPIdRootElement) {
			HAPIdRootElement rootEleId = (HAPIdRootElement)obj;
			return HAPUtilityBasic.isEquals(this.m_rootName, rootEleId.m_rootName) && HAPUtilityBasic.isEquals(this.m_valueStructureId, rootEleId.m_valueStructureId);
		}
		return false;
	}
}
