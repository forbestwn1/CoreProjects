package com.nosliw.core.xxx.application.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPIdValueStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUEPORTID = "valuePortId";
	
	@HAPAttribute
	public static final String VALUESTRUCTUREID = "valueStructureId";
	
	private HAPIdValuePortInBundle m_valuePortId;
	
	private String m_valueStructureId;

	public HAPIdValueStructure(HAPIdValuePortInBundle valuePortId, String valueStructureId) {
		this.m_valuePortId = valuePortId;
		this.m_valueStructureId = valueStructureId;
	}

	public HAPIdValuePortInBundle getValuePortId() {    return this.m_valuePortId;    }
	
	public String getValueStructureId() {    return this.m_valueStructureId;     }
	
	public String getKey() {
		return HAPUtilityNamingConversion.cascadeElements(new String[] {this.m_valuePortId.getKey(), this.m_valueStructureId}, HAPConstantShared.SEPERATOR_LEVEL1); 
	}
	
	@Override
	protected String buildLiterate(){  return this.getKey();	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_valuePortId!=null) {
			jsonMap.put(VALUEPORTID, this.m_valuePortId.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTUREID, this.m_valueStructureId);
	}
	
	@Override
	public int hashCode() {		return this.buildLiterate().hashCode();	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if(obj instanceof HAPIdValueStructure) {
//			HAPIdValueStructure rootEleId = (HAPIdValueStructure)obj;
//			return HAPUtilityBasic.isEquals(this.m_rootName, rootEleId.m_rootName) && HAPUtilityBasic.isEquals(this.m_valueStructureId, rootEleId.m_valueStructureId);
//		}
//		return false;
//	}

}
