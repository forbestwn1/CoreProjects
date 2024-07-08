package com.nosliw.core.application.division.manual.executable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

@HAPEntityWithAttribute
public class HAPInfoBrickType extends HAPSerializableImp{

	@HAPAttribute
	public static String BRICKTYPE = "brickType";

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	@HAPAttribute
	public static String TASKTYPE = "taskType";
	
	//entity type
	private HAPIdBrickType m_brickTypeId;
	
	//
	private Boolean m_isComplex = null;
	
	private String m_taskType;
	
	public HAPInfoBrickType() {
		this.m_brickTypeId = null;
	}
	
	public HAPInfoBrickType(HAPIdBrickType brickTypeId, boolean isComplex) {
		this.m_brickTypeId = brickTypeId;
		this.m_isComplex = isComplex;
	}
	
	public HAPInfoBrickType(HAPIdBrickType brickTypeId, boolean isComplex, String taskType) {
		this.m_brickTypeId = brickTypeId;
		this.m_isComplex = isComplex;
		this.m_taskType = taskType;
	}
	
	public HAPInfoBrickType(HAPIdBrickType brickTypeId) {		this.m_brickTypeId = brickTypeId;	}
	public HAPIdBrickType getBrickTypeId() {    return this.m_brickTypeId;    }
	
	public Boolean getIsComplex() {   return this.m_isComplex;  }
	
	public String getTaskType() {    return this.m_taskType;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(BRICKTYPE, this.m_brickTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
		jsonMap.put(TASKTYPE, this.m_taskType);
	}
	
}
