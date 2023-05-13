package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableWrapperTask<T extends HAPExecutableTask> extends HAPExecutableGroupDataAssociationForTask{

	@HAPAttribute
	public static String TASK = "task";
	
	private T m_task;

	public HAPExecutableWrapperTask() {	}

	public void setTask(T task) {  this.m_task = task;  }
	public T getTask() {  return this.m_task;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		
		return true;  
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_task!=null)   jsonMap.put(TASK, this.m_task.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		this.buildResourceDependencyForExecutable(dependency, m_task, runtimeInfo, resourceManager);
	}

}
