package com.nosliw.data.core.dataassociation.mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.dataassociation.HAPOutputStructure;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

@HAPEntityWithAttribute
public class HAPExecutableDataAssociationMapping extends HAPExecutableDataAssociationImp{

	@HAPAttribute
	public static String ITEM = "item";

	@HAPAttribute
	public static String MAPPINGPATH = "mappingPath";

	//data association output context
	private List<HAPItemValueMapping<HAPIdRootElement>> m_items;
	
	//path mapping for relative node (output path in context - input path in context) during runtime
	private List<HAPPathValueMapping> m_relativePathMapping;
	
	private Map<String, Object> m_constantAssignment;
	
	private Set<String> m_inputDependency;

	public HAPExecutableDataAssociationMapping() {}
	
	public HAPExecutableDataAssociationMapping(HAPDefinitionDataAssociationMapping definition, HAPContainerStructure input, HAPContainerStructure output) {
		super(definition, input, output);
		this.m_items = new ArrayList<HAPItemValueMapping<HAPIdRootElement>>();
		this.m_relativePathMapping = new ArrayList<HAPPathValueMapping>();
		this.m_inputDependency = new HashSet<String>();
	}
	
	public Set<String> getInputDependency(){   return this.m_inputDependency;    }
	
	@Override
	public HAPContainerStructure getInput() {
		HAPContainerStructure out = new HAPContainerStructure();
		for(String dependencyName : this.m_inputDependency) {
			out.addStructure(dependencyName, super.getInput().getStructure(dependencyName));
		}
		return out;
	}

	@Override
	public HAPOutputStructure getOutput() {
		HAPOutputStructure out = new HAPOutputStructure();
//		for(String name : this.m_mappings.keySet()) {
//			out.addOutputStructure(name, super.getOutput().getOutputStructure(name));
//		}
		return out;
	}

	public void addItem(HAPItemValueMapping<HAPIdRootElement> valueMapping) {	this.m_items.add(valueMapping);	}
	
	public void setConstantAssignments(Map<String, Object> constantAssignment) {     this.m_constantAssignment = constantAssignment;      }
	public Map<String, Object> getConstantAssignments(){    return this.m_constantAssignment;   }

	public void addRelativePathMapping(HAPPathValueMapping mappingPath) {    this.m_relativePathMapping.add(mappingPath);     }
	public void addRelativePathMappings(List<HAPPathValueMapping> mappingPaths) {    this.m_relativePathMapping.addAll(mappingPaths);    }
	public List<HAPPathValueMapping> getRelativePathMappings() {  return this.m_relativePathMapping;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAPPINGPATH, HAPSerializeManager.getInstance().toStringValue(this.m_relativePathMapping, HAPSerializationFormat.JSON));
		jsonMap.put(ITEM, HAPSerializeManager.getInstance().toStringValue(this.m_items, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;  
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		for(HAPPathValueMapping mappingPath : this.m_relativePathMapping) {
			dependency.addAll(mappingPath.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}