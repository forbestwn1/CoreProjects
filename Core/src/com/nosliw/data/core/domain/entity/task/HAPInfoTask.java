package com.nosliw.data.core.domain.entity.task;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoTask extends HAPSerializableImp{

	@HAPAttribute
	public static final String ENTITYPATH = "entityPath";

	@HAPAttribute
	public static final String TASKTYPE = "taskType";

	@HAPAttribute
	public static final String ADAPTERINFO = "adapterInfo";

	private HAPPath m_entityPath;
	
	private String m_taskType;

	private HAPInfoAdapter m_adapterInfo;
	
	public HAPInfoTask(HAPPath entityPath, String taskType, HAPInfoAdapter adapterInfo) {
		this.m_entityPath = entityPath;
		this.m_taskType = taskType;
		this.m_adapterInfo = adapterInfo;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYPATH, this.m_entityPath.toString());
		jsonMap.put(TASKTYPE, this.m_taskType);
		if(this.m_adapterInfo!=null) jsonMap.put(ADAPTERINFO, this.m_adapterInfo.toStringValue(HAPSerializationFormat.JSON));
	}
}
