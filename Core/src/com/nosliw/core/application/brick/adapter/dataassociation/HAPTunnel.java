package com.nosliw.core.application.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.valueport.HAPReferenceValuePort;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPTunnel extends HAPExecutableImp{

	@HAPAttribute
	public static String FROMENDPOINT = "fromEndPoint";

	@HAPAttribute
	public static String TOENDPOINT = "toEndPoint";

	@HAPAttribute
	public static String MATCHERS = "matchers";
	
	private HAPEndpointInTunnel m_fromEndPoint;
	
	private HAPEndpointInTunnel m_toEndPoint;
	
	private HAPMatchers m_matchers;
	
	public HAPTunnel(HAPEndpointInTunnel fromEndPoint, HAPEndpointInTunnel toEndPoint, HAPMatchers matchers) {
		this.m_fromEndPoint = fromEndPoint;
		this.m_toEndPoint = toEndPoint;
		this.m_matchers = matchers;
	}


	public HAPReferenceValuePort getFromValuePortRef() {   return this.m_fromValuePortRef;     }
	public HAPReferenceValuePort getToValuePortRef() {    return this.m_toValuePortRef;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FROMCONSTANT, HAPUtilityJson.buildJson(m_fromConstant, HAPSerializationFormat.JSON));
		
		jsonMap.put(FROMPROVIDENAME, this.m_fromProvideName);
		jsonMap.put(FROMPROVIDEPATH, this.m_fromProvidePath);
		
		jsonMap.put(FROMVALUEPORTID, this.m_fromValuePortRef.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(FROMVALUESTRUCTUREID, m_fromValueStructureId);
		jsonMap.put(FROMITEMPATH, this.m_fromItemPath);

		jsonMap.put(MATCHERS, HAPUtilityJson.buildJson(m_matchers, HAPSerializationFormat.JSON));

		jsonMap.put(TOVALUEPORTID, this.m_toValuePortRef.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TOVALUESTRUCTUREID, m_toValueStructureId);
		jsonMap.put(TOITEMPATH, this.m_toItemPath);
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManager resourceManager) {
		List<HAPResourceIdSimple> ids = new ArrayList<HAPResourceIdSimple>();
		ids.addAll(HAPMatcherUtility.getMatchersResourceId(m_matchers));
		dependency.addAll(HAPUtilityResourceId.buildResourceDependentFromResourceId(ids));
	}
}
