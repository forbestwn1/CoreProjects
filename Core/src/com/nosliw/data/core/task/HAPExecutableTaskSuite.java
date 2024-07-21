package com.nosliw.data.core.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure1.HAPExecutableValueStructure;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructureScript;

@HAPEntityWithAttribute
public class HAPExecutableTaskSuite extends HAPExecutableImp implements HAPWithEntityElement<HAPExecutableTask>{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String TASK = "task";

	@HAPAttribute
	public static String INITSCRIPT = "initScript";

	private String m_id;

	private Map<String, HAPExecutableTask> m_tasks;

	private HAPManualBrickWrapperValueStructure m_valueStructureWrapper;

	public HAPExecutableTaskSuite(String id) {
		this.m_tasks = new LinkedHashMap<String, HAPExecutableTask>();
		this.m_id = id;
	}

	@Override
	public Set<HAPExecutableTask> getEntityElements() {   return new HashSet<HAPExecutableTask>(this.m_tasks.values());  }

	@Override
	public HAPExecutableTask getEntityElement(String id) {   return this.m_tasks.get(id);  }

	@Override
	public void addEntityElement(HAPExecutableTask entityElement) {   this.m_tasks.put(entityElement.getId(), entityElement);  }

	public void setValueStructureDefinitionWrapper(HAPManualBrickWrapperValueStructure valueStructureWrapper) {   	this.m_valueStructureWrapper = valueStructureWrapper;	}
	
	public HAPManualBrickWrapperValueStructure getValueStructureDefinitionWrapper() {    return this.m_valueStructureWrapper;    }

	public HAPExecutableValueStructure getValueStructureExe() {
		return HAPUtilityValueStructure.buildExecuatableValueStructure(this.getValueStructureDefinitionWrapper().getValueStructureBlock());
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {		
		//process resources
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		 for(HAPExecutableTask task : this.m_tasks.values()) {
			 out.addAll(task.getResourceDependency(runtimeInfo, resourceManager));
		 }
		return out;	
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		Map<String, String> activityJsonMap = new LinkedHashMap<String, String>();
		for(String taskId : this.m_tasks.keySet()) {
			activityJsonMap.put(taskId, this.m_tasks.get(taskId).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(TASK, HAPUtilityJson.buildMapJson(activityJsonMap));

		jsonMap.put(INITSCRIPT, HAPUtilityValueStructureScript.buildValueStructureInitScript(this.getValueStructureExe()).getScript());
		typeJsonMap.put(INITSCRIPT, HAPJsonTypeScript.class);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(TASK, HAPUtilityJson.buildJson(this.m_tasks, HAPSerializationFormat.JSON));
	}

}
