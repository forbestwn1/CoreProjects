package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.structure.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;

public class HAPLoopActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String CONTAINERNAME = "containerName";

	@HAPAttribute
	public static String ELEMENTNAME = "elementName";

	@HAPAttribute
	public static String INDEXNAME = "indexName";

	@HAPAttribute
	public static String STEP = "step";

	@HAPAttribute
	public static String PROCESS = "process";

	private String m_containerName;

	private String m_indexName;

	private String m_elementName;

	//step in loop, include input mapping, output mapping, and process for this step
	private HAPDefinitionWrapperTask<HAPResourceId> m_step;

	public HAPLoopActivityDefinition(String type) {
		super(type);
	}
	
	public String getContainerName(){  return this.m_containerName;    }
	public String getIndexName(){  return this.m_indexName;    }
	public String getElementName(){  return this.m_elementName;    }
	public HAPDefinitionWrapperTask<HAPResourceId> getStep(){   return this.m_step;   }
	
	@Override
	public HAPStructureValueDefinition getInputContextStructure(HAPStructureValueDefinition parentContextStructure) {  return parentContextStructure;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);

		this.buildDefaultInputMapping();
		this.buildDefaultResultOutputMapping();
		
		JSONObject jsonObj = (JSONObject)json;
		this.m_containerName = jsonObj.optString(CONTAINERNAME);
		this.m_indexName = jsonObj.optString(INDEXNAME);
		this.m_elementName = jsonObj.optString(ELEMENTNAME);
		
		JSONObject stepObj = jsonObj.optJSONObject(STEP);
		HAPResourceId process = HAPFactoryResourceId.newInstance(stepObj.opt(PROCESS));
		this.m_step = new HAPDefinitionWrapperTask();
		this.m_step.buildObj(stepObj, process);
		
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);

		jsonMap.put(CONTAINERNAME, this.m_containerName);
		jsonMap.put(INDEXNAME, this.m_indexName);
		jsonMap.put(ELEMENTNAME, this.m_elementName);
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPLoopActivityDefinition out = new HAPLoopActivityDefinition(this.getType());
		this.cloneToNormalActivityDefinition(out);
		out.m_containerName = this.m_containerName;
		out.m_indexName = this.m_indexName;
		out.m_elementName = this.m_elementName;
		out.m_step = this.m_step.clone();
		return out;
	}
}
