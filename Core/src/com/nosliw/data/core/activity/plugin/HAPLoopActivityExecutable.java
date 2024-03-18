package com.nosliw.data.core.activity.plugin;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.activity.HAPPluginResourceIdActivity;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.process1.HAPActivityPluginId;
import com.nosliw.data.core.process1.HAPExecutableActivityNormal;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

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
	private HAPReferenceElementInStructure m_containerDataPath;
	
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
	public void setContainerDataPath(HAPReferenceElementInStructure path) {    this.m_containerDataPath = path;   }
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		dependency.add(new HAPResourceDependency(new HAPPluginResourceIdActivity(new HAPActivityPluginId(HAPConstantShared.ACTIVITY_TYPE_PROCESS))));
		this.buildResourceDependencyForExecutable(dependency, m_step, runtimeInfo, resourceManager);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_step = HAPParserDataAssociation.buildExecutableWrapperTask(jsonObj, new HAPExecutableProcess(m_activityPluginMan));
		this.m_containerDataPath = new HAPReferenceElementInStructure();
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
