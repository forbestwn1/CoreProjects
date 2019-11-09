package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

public class HAPLoopActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String STEP = "step";

	@HAPAttribute
	public static String CONTAINERDATAPATH = "containerDataPath";

	@HAPAttribute
	public static String ELEMENTNAME = "elementName";

	@HAPAttribute
	public static String INDEXNAME = "indexName";

	private HAPExecutableWrapperTask m_step;
	
	//path for container data
	private HAPContextPath m_containerDataPath;
	
	public HAPLoopActivityExecutable(String id, HAPLoopActivityDefinition activityDef) {
		super(id, activityDef);
	}

	public HAPLoopActivityDefinition getLoopActivityDefinition() {   return (HAPLoopActivityDefinition)this.getActivityDefinition();   }
	public void setStep(HAPExecutableWrapperTask step) {  this.m_step = step;   }
	public HAPExecutableWrapperTask getStep() {  return this.m_step;   }
	public void setContainerDataPath(HAPContextPath path) {    this.m_containerDataPath = path;   }
	
	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		out.add(new HAPResourceDependent(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstant.ACTIVITY_TYPE_PROCESS))));
		out.addAll(this.m_step.getResourceDependency(runtimeInfo));
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTAINERDATAPATH, this.m_containerDataPath.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(INDEXNAME, this.getLoopActivityDefinition().getIndexName());
		jsonMap.put(ELEMENTNAME, this.getLoopActivityDefinition().getElementName());
		jsonMap.put(STEP, this.m_step.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(STEP, this.m_step.toResourceData(runtimeInfo).toString());
	}	
}
