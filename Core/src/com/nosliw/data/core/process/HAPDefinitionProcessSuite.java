package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

//application that contains multiple tasks
@HAPEntityWithAttribute
public class HAPDefinitionProcessSuite extends HAPResourceDefinitionContainer{

	@HAPAttribute
	public static String ELEMENT = "element";

	private Map<String, HAPDefinitionProcessSuiteElement> m_elements;

	public HAPDefinitionProcessSuite() {
		this.m_elements = new LinkedHashMap<String, HAPDefinitionProcessSuiteElement>();
	}

	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESSSUITE;  }

	@Override
	public HAPComponent getElement(String name) {		return (HAPComponent)this.getProcessElement(name);	}

	public HAPDefinitionProcessSuiteElement getProcessElement(String processId) {  return this.m_elements.get(processId);   }
	public Map<String, HAPDefinitionProcessSuiteElement> getProcesses(){   return this.m_elements;   }

	public void addProcess(String id, HAPDefinitionProcessSuiteElement process) {
		String type = process.getType();
		if(type.equals(HAPConstant.PROCESSSUITE_ELEMENTTYPE_ENTITY)) {
			((HAPDefinitionProcessSuiteElementEntity)process).getAttachmentContainer().merge(this.getAttachmentContainer(), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		}
		this.m_elements.put(id, process);  
	}

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPDefinitionProcessSuite clone() {
		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();
		this.cloneToComplexResourceDefinition(out);
		for(String eleName : this.m_elements.keySet()) {
			out.addProcess(eleName, this.m_elements.get(eleName));
		}
		return out;
	}

	public HAPDefinitionProcessSuite cloneProcessSuiteDefinition() {
		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();
		this.cloneToResourceDefinitionContainer(out);
		for(String name : this.m_elements.keySet()) {
			out.m_elements.put(name, this.m_elements.get(name).cloneProcessSuiteElementDefinition());
		}
		return out;
	}

	@Override
	public HAPResourceDefinitionContainer cloneResourceDefinitionContainer() {	return this.cloneProcessSuiteDefinition();	}

}
