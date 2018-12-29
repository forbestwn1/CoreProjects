package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutable;

@HAPEntityWithAttribute
public abstract class HAPExecutableActivity extends HAPSerializableImp implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String DEFINITION = "definition";
	
	private String m_id;
	
	private HAPDefinitionActivity m_activityDefinition;
	
	public HAPExecutableActivity(String id, HAPDefinitionActivity activityDef) {
		this.m_id = id;
		this.m_activityDefinition = activityDef;
	}
	
	public String getType() {   return this.getActivityDefinition().getType();  }
	
	public String getId() {  return this.m_id;  }
	
	public HAPDefinitionActivity getActivityDefinition() {   return this.m_activityDefinition;   }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(DEFINITION, this.m_activityDefinition.toStringValue(HAPSerializationFormat.JSON));
	}
}
