package com.nosliw.data.core.domain.entity.script.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.domain.entity.task.HAPRequirementTask;

@HAPEntityWithAttribute
public class HAPDefinitionTaskScript extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String REQUIREMENT = "requirement";

	private List<HAPRequirementTask> m_requirement;
	
	public HAPDefinitionTaskScript() {
		this.m_requirement = new ArrayList<HAPRequirementTask>();
	}
	
	public List<HAPRequirementTask> getRequiredInteface(){		return this.m_requirement;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REQUIREMENT, HAPSerializeManager.getInstance().toStringValue(m_requirement, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		JSONArray requireArray = jsonObj.optJSONArray(REQUIREMENT);
		if(requireArray!=null) {
			for(int i=0; i<requireArray.length(); i++) {
				JSONObject requireJsonObj = requireArray.getJSONObject(i);
				HAPRequirementTask require = new HAPRequirementTask();
				require.buildObject(requireJsonObj, HAPSerializationFormat.JSON);
				this.m_requirement.add(require);
			}
		}
		return true;  
	}

}
