package com.nosliw.data.core.task;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * A group of expression definitions 
 */
@HAPEntityWithAttribute
public class HAPDefinitionTaskSuite extends HAPDefinitionComponent {

	@HAPAttribute
	public static String TASKS = "tasks";

	//tasks
	private Map<String, HAPDefinitionTask> m_tasks;
	
	public HAPDefinitionTaskSuite(){
		this.m_tasks = new LinkedHashMap<String, HAPDefinitionTask>();
	}
	
	public HAPDefinitionTask getTask(String name){  return this.m_tasks.get(name); }

	//get all expression definitions in suite
	public Map<String, HAPDefinitionTask> getAllTasks(){  return this.m_tasks;  }

	
	//configuration for suite
	public Map<String, String> getConfigure(){  return null;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		try{
			JSONObject jsonObj = (JSONObject)json;
			{
				JSONArray tasksArray = jsonObj.optJSONArray(TASKS);
				for(int i=0; i<tasksArray.length(); i++){
					HAPDefinitionTask task = HAPTaskDefinitionManager.buildTask(tasksArray.get(i));
					this.m_tasks.put(task.getName(), task);
				}
			}
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
