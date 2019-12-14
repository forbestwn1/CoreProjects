package com.nosliw.data.core.script.context.dataassociation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public class HAPExecutableWrapperTask<T extends HAPExecutableTask> extends HAPExecutableImp{

	@HAPAttribute
	public static String OUTPUTMAPPING = "outputMapping";

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	@HAPAttribute
	public static String TASK = "task";
	
	private HAPExecutableDataAssociation m_inputMapping;
	
	//data association from process to external
	private Map<String, HAPExecutableDataAssociation> m_outputMapping;

	private T m_task;

	public HAPExecutableWrapperTask() {
		this.m_outputMapping = new LinkedHashMap<String, HAPExecutableDataAssociation>();
	}

	public void setTask(T task) {  this.m_task = task;  }
	public T getTask() {  return this.m_task;  }
	public HAPExecutableDataAssociation getInputMapping() {   return this.m_inputMapping;   }
	public void setInputMapping(HAPExecutableDataAssociation inputMapping) {  this.m_inputMapping = inputMapping;  }
	public Map<String, HAPExecutableDataAssociation> getOutputMapping(){    return this.m_outputMapping;     }
	public HAPExecutableDataAssociation getOutputMapping(String name) {   return this.m_outputMapping.get(name);   }
	public void addOutputMapping(String resultName, HAPExecutableDataAssociation dataAssociation) {   this.m_outputMapping.put(resultName, dataAssociation);   }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(!this.m_outputMapping.isEmpty())   jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		if(this.m_inputMapping!=null)   jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
		if(this.m_task!=null)   jsonMap.put(TASK, HAPJsonUtility.buildJson(this.m_task, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);
		
		if(this.m_inputMapping!=null) 	jsonMap.put(INPUTMAPPING, this.m_inputMapping.toResourceData(runtimeInfo).toString());
		
		if(!this.m_outputMapping.isEmpty()) {
			Map<String, String> outputMappingMap = new LinkedHashMap<String, String>();
			for(String resultName : this.m_outputMapping.keySet()) {
				outputMappingMap.put(resultName, this.m_outputMapping.get(resultName).toResourceData(runtimeInfo).toString());
			}
			jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildMapJson(outputMappingMap));
		}
		
		if(this.m_task!=null)   jsonMap.put(TASK, this.m_task.toResourceData(runtimeInfo).toString());
		
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		if(this.m_inputMapping!=null)  out.addAll(this.m_inputMapping.getResourceDependency(runtimeInfo));
		for(String resultName : this.m_outputMapping.keySet()) {
			out.addAll(this.m_outputMapping.get(resultName).getResourceDependency(runtimeInfo));
		}
		out.addAll(this.m_task.getResourceDependency(runtimeInfo));
		return out;
	}

}
