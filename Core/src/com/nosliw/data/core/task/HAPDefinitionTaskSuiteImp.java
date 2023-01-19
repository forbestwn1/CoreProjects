package com.nosliw.data.core.task;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;

public class HAPDefinitionTaskSuiteImp extends HAPEntityInfoImp implements HAPDefinitionTaskSuite{

	private Map<String, HAPDefinitionTask> m_tasks;
	
	private HAPWithValueContext m_withValueStructure;
	
	public HAPDefinitionTaskSuiteImp(HAPWithValueContext withValueStructure) {
		this.m_tasks = new LinkedHashMap<String, HAPDefinitionTask>();
		this.m_withValueStructure = withValueStructure;
	}
	
	@Override
	public Set<HAPDefinitionTask> getEntityElements() {  return new HashSet<HAPDefinitionTask>(this.m_tasks.values()); }

	@Override
	public HAPDefinitionTask getEntityElement(String id) {   return this.m_tasks.get(id);  }

	@Override
	public void addEntityElement(HAPDefinitionTask entityElement) {  this.m_tasks.put(entityElement.getId(), entityElement); }

	@Override
	public HAPDefinitionWrapperValueStructure getValueStructureWrapper() {    return this.m_withValueStructure.getValueStructureWrapper();   }

	@Override
	public void setValueStructureWrapper(HAPDefinitionWrapperValueStructure valueStructureWrapper) {    throw new RuntimeException();  }

	@Override
	public HAPDefinitionTaskSuite cloneTaskSuiteDefinition() {
		HAPDefinitionTaskSuiteImp out = new HAPDefinitionTaskSuiteImp(this.m_withValueStructure);
		for(String name : this.m_tasks.keySet()) {
			out.m_tasks.put(name, this.m_tasks.get(name));
		}
		return out;
	}
}
