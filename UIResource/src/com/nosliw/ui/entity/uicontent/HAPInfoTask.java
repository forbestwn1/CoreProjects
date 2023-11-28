package com.nosliw.ui.entity.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPInfoTask extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String PATH = "path";

	@HAPAttribute
	public static final String TASKTYPE = "taskType";

	private String m_name;
	
	private HAPPath m_path;
	
	private String m_taskType;
	
	public HAPInfoTask(String name, HAPPath path, String taskType) {
		this.m_name = name;
		this.m_path = path;
		this.m_taskType = taskType;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(PATH, this.m_path.toString());
		jsonMap.put(TASKTYPE, this.m_taskType);
	}
}
