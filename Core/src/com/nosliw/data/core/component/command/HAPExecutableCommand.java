package com.nosliw.data.core.component.command;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.HAPExecutableGroupDataAssociationForTask;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableCommand extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String TASK = "task";
	
	@HAPAttribute
	public static String DATAASSOCIATION = "dataAssociation";

	@HAPAttribute
	public static String COMMAND = "command";

	private String m_task;
	
	private HAPExecutableGroupDataAssociationForTask m_dataAssociations;
	
	private HAPDefinitionCommand m_command;
	
	public HAPExecutableCommand() {
		this.m_dataAssociations = new HAPExecutableGroupDataAssociationForTask();
	}
	
	public void setTaskName(String task) {    this.m_task = task;    }
	public String getTaskName() {     return this.m_task;     }
	
	public HAPExecutableGroupDataAssociationForTask getDataAssociations() {   return this.m_dataAssociations;    }
	
	public void setCommand(HAPDefinitionCommand command) {     this.m_command = command;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASK, this.m_task);
		if(this.m_dataAssociations!=null)   jsonMap.put(DATAASSOCIATION, HAPJsonUtility.buildJson(this.m_dataAssociations, HAPSerializationFormat.JSON));
		jsonMap.put(COMMAND, HAPJsonUtility.buildJson(this.m_command, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_dataAssociations!=null) 	jsonMap.put(DATAASSOCIATION, this.m_dataAssociations.toResourceData(runtimeInfo).toString());
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		this.buildResourceDependencyForExecutable(dependency, m_dataAssociations, runtimeInfo, resourceManager);
	}

}
