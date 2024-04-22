package com.nosliw.core.application.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableDataAssociationMapping extends HAPExecutableDataAssociationImp{

	@HAPAttribute
	public static String TUNNEL = "tunnel";

	//path mapping for relative node (output path in context - input path in context) during runtime
	private List<HAPTunnel> m_tunnel;
	
	private Set<String> m_inputDependency;

	public HAPExecutableDataAssociationMapping() {}
	
	public HAPExecutableDataAssociationMapping(HAPDefinitionDataAssociationMapping definition) {
		super(definition);
		this.m_tunnel = new ArrayList<HAPTunnel>();
		this.m_inputDependency = new HashSet<String>();
	}
	
	public Set<String> getInputDependency(){   return this.m_inputDependency;    }
	
	public void addTunnel(HAPTunnel mappingPath) {    this.m_tunnel.add(mappingPath);     }
	public void addRelativePathMappings(List<HAPTunnel> mappingPaths) {    this.m_tunnel.addAll(mappingPaths);    }
	public List<HAPTunnel> getTunnels() {  return this.m_tunnel;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TUNNEL, HAPSerializeManager.getInstance().toStringValue(this.m_tunnel, HAPSerializationFormat.JSON));
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
		for(HAPTunnel mappingPath : this.m_tunnel) {
			dependency.addAll(mappingPath.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
