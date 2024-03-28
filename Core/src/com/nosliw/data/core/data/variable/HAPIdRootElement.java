package com.nosliw.data.core.data.variable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.valueport.HAPReferenceValuePort;

@HAPEntityWithAttribute
public class HAPIdRootElement extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUEPORTID = "valuePortId";
	
	@HAPAttribute
	public static final String VALUESTRUCTUREID = "valueStructureId";
	
	@HAPAttribute
	public static final String ROOTNAME = "rootName";
	
	private HAPReferenceValuePort m_refValuePort;
	
	private String m_valueStructureId;
	
	private String m_rootName;
	
	public HAPIdRootElement(String reference) {
		String[] parts = HAPUtilityNamingConversion.splitTextByTwoPart(reference, HAPConstantShared.SEPERATOR_LEVEL1);
		this.m_rootName = parts[0];
		if(parts.length>1) {
			this.m_valueStructureId = parts[1];
		}
		if(parts.length>2) {
			this.m_refValuePort = parts[2];
		}
	}
	
	public HAPIdRootElement(HAPReferenceValuePort valuePortId, String valueStructureId, String rootName) {
		this.m_refValuePort = valuePortId;
		this.m_valueStructureId = valueStructureId;
		this.m_rootName = rootName;
	}
	
	public HAPIdRootElement(String valueStructureId, String rootName) {
		this(null, valueStructureId, rootName);
	}

	public HAPReferenceValuePort getValuePortRef() {    return this.m_refValuePort;    }
	
	public String getValueStructureId() {    return this.m_valueStructureId;     }
	
	public String getRootName() {    return this.m_rootName;   }

	public HAPComplexPath getPath() {    return new HAPComplexPath(this.m_valueStructureId, this.m_rootName);     }
	
	public String getKey() {
		return HAPUtilityNamingConversion.cascadeElements(new String[] {this.m_rootName, this.m_valueStructureId}, HAPConstantShared.SEPERATOR_LEVEL1); 
	}
	
	@Override
	protected String buildLiterate(){  return this.getKey();	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_refValuePort!=null) {
			jsonMap.put(VALUEPORTID, this.m_refValuePort.toStringValue(HAPSerializationFormat.JSON));
		}
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
