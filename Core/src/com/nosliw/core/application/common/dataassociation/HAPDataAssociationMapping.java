package com.nosliw.core.application.common.dataassociation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPDataAssociationMapping extends HAPDataAssociation{

	@HAPAttribute
	public static String TUNNEL = "tunnel";

	//path mapping for relative node (output path in context - input path in context) during runtime
	private List<HAPTunnel> m_tunnel;
	
	private Set<String> m_inputDependency;

	public HAPDataAssociationMapping() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING);
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
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		for(HAPTunnel mappingPath : this.m_tunnel) {
			dependency.addAll(mappingPath.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
