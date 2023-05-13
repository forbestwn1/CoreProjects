package com.nosliw.data.core.component.command;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;
import com.nosliw.data.core.interactive.HAPDefinitionInteractiveImpBasic;

public class HAPDefinitionCommand extends HAPDefinitionInteractiveImpBasic{

	@HAPAttribute
	public static String TASK = "task";

	private String m_task;
	
	public HAPDefinitionCommand cloneCommandDefinition() {
		HAPDefinitionCommand out = new HAPDefinitionCommand();
		this.cloneToInteractive(out);
		out.m_task = this.m_task;
		return out;
	}

	public String getTaskName() {     return this.m_task;     }
	
	@Override
	public HAPDefinitionInteractive cloneInteractiveDefinition() {  return this.cloneCommandDefinition();  }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject objJson = (JSONObject)json;
		super.buildObjectByJson(objJson);
		this.m_task = objJson.getString(TASK);
		return true;  
	}

}
