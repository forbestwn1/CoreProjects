package com.nosliw.core.application.division.manual.executable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPInfoBrickType extends HAPSerializableImp{

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	@HAPAttribute
	public static String TASKTYPE = "taskType";
	
	//
	private Boolean m_isComplex = null;
	
	private String m_taskType;
	
	public HAPInfoBrickType() {
		this.m_isComplex = false;
	}
	
	public HAPInfoBrickType(boolean isComplex) {
		this.m_isComplex = isComplex;
	}
	
	public HAPInfoBrickType(boolean isComplex, String taskType) {
		this.m_isComplex = isComplex;
		this.m_taskType = taskType;
	}
	
	public Boolean getIsComplex() {   return this.m_isComplex;  }
	
	public String getTaskType() {    return this.m_taskType;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
		jsonMap.put(TASKTYPE, this.m_taskType);
	}
	
}
