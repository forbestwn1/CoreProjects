package com.nosliw.data.core.process;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public abstract class HAPExecutableActivity extends HAPSerializableImp implements HAPExecutable{

	private String m_id;
	
	private HAPDefinitionActivity m_activityDefinition;
	
	public HAPExecutableActivity(String id, HAPDefinitionActivity activityDef) {
		this.m_id = id;
		this.m_activityDefinition = activityDef;
	}
	
	public String getId() {  return this.m_id;  }
	
	public HAPDefinitionActivity getActivityDefinition() {   return this.m_activityDefinition;   }
	
}
