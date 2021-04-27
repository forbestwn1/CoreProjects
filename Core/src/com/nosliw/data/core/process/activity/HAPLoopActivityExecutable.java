package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.structure.dataassociation.HAPParserDataAssociation;

public class HAPLoopActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String STEP = "step";

	@HAPAttribute
	public static String CONTAINERDATAPATH = "containerDataPath";

	@HAPAttribute
	public static String ELEMENTNAME = "elementName";

	@HAPAttribute
	public static String INDEXNAME = "indexName";

	private String m_indexName;

	private String m_elementName;

	private HAPExecutableWrapperTask<HAPExecutableProcess> m_step;
	
	//path for container data
	private HAPReferenceElement m_containerDataPath;
	
	private HAPManagerActivityPlugin m_activityPluginMan;

	public HAPLoopActivityExecutable(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	public HAPLoopActivityExecutable(String id, HAPLoopActivityDefinition activityDef) {
		super(id, activityDef);
		this.m_indexName = activityDef.getIndexName();
		this.m_elementName = activityDef.getElementName();
	}

//	public HAPLoopActivityDefinition getLoopActivityDefinition() {   return (HAPLoopActivityDefinition)this.getActivityDefinition();   }
	public void setStep(HAPExecutableWrapperTask<HAPExecutableProcess> step) {  this.m_step = step;   }
	public HAPExecutableWrapperTask<HAPExecutableProcess> getStep() {  return this.m_step;   }
	public void setContainerDataPath(HAPReferenceElement path) {    this.m_containerDataPath = path;   }
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstantShared.ACTIVITY_TYPE_PROCESS))));
		out.addAll(this.m_step.getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_step = HAPParserDataAssociation.buildExecutableWrapperTask(jsonObj, new HAPExecutableProcess(m_activityPluginMan));
		this.m_containerDataPath = new HAPReferenceElement();
		this.m_containerDataPath.buildObject(jsonObj.getJSONObject(CONTAINERDATAPATH), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTAINERDATAPATH, this.m_containerDataPath.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(INDEXNAME, this.m_indexName);
		jsonMap.put(ELEMENTNAME, this.m_elementName);
		jsonMap.put(STEP, this.m_step.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(STEP, this.m_step.toResourceData(runtimeInfo).toString());
	}	
}
