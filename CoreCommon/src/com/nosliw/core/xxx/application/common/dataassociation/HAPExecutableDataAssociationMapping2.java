package com.nosliw.core.xxx.application.common.dataassociation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.core.application.common.dataassociation.HAPTunnel;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.valueport.HAPIdRootElement;
import com.nosliw.core.xxx.application.common.structure.HAPElementStructureUnknown;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPItemValueMapping;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableDataAssociationMapping2 extends HAPExecutableDataAssociationImp{

	@HAPAttribute
	public static String ITEM = "item";

	@HAPAttribute
	public static String MAPPINGPATH = "mappingPath";

	@HAPAttribute
	public static String PROVIDE = "provide";

	//data association output context
	private List<HAPItemValueMapping<HAPIdRootElement>> m_items;
	
	//path mapping for relative node (output path in context - input path in context) during runtime
	private List<HAPTunnel> m_relativePathMapping;
	
	private Map<String, HAPElementStructure> m_providers;
	
	private Map<String, Object> m_constantAssignment;
	
	private Set<String> m_inputDependency;

	public HAPExecutableDataAssociationMapping2() {}
	
	public HAPExecutableDataAssociationMapping2(HAPDefinitionDataAssociationMapping definition) {
		super(definition);
		this.m_items = new ArrayList<HAPItemValueMapping<HAPIdRootElement>>();
		this.m_relativePathMapping = new ArrayList<HAPTunnel>();
		this.m_providers = new LinkedHashMap<String, HAPElementStructure>();
		this.m_inputDependency = new HashSet<String>();
	}
	
	public Set<String> getInputDependency(){   return this.m_inputDependency;    }
	
	public void addItem(HAPItemValueMapping<HAPIdRootElement> valueMapping) {	this.m_items.add(valueMapping);	}
	
	public void setConstantAssignments(Map<String, Object> constantAssignment) {     this.m_constantAssignment = constantAssignment;      }
	public Map<String, Object> getConstantAssignments(){    return this.m_constantAssignment;   }

	public void addRelativePathMapping(HAPTunnel mappingPath) {    this.m_relativePathMapping.add(mappingPath);     }
	public void addRelativePathMappings(List<HAPTunnel> mappingPaths) {    this.m_relativePathMapping.addAll(mappingPaths);    }
	public List<HAPTunnel> getRelativePathMappings() {  return this.m_relativePathMapping;  }

	public void addProvide(String name, HAPElementStructure def) {
		if(def==null)  def = new HAPElementStructureUnknown();
		this.m_providers.put(name, def);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAPPINGPATH, HAPManagerSerialize.getInstance().toStringValue(this.m_relativePathMapping, HAPSerializationFormat.JSON));
		jsonMap.put(ITEM, HAPManagerSerialize.getInstance().toStringValue(this.m_items, HAPSerializationFormat.JSON));
		jsonMap.put(PROVIDE, HAPManagerSerialize.getInstance().toStringValue(this.m_providers, HAPSerializationFormat.JSON));
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
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		for(HAPTunnel mappingPath : this.m_relativePathMapping) {
			dependency.addAll(mappingPath.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
