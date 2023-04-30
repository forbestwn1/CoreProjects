package com.nosliw.data.core.domain.entity.dataassociation.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPPathValueMapping extends HAPExecutableImp{

	@HAPAttribute
	public static String FROMDOMAINNAME = "fromDomainName";
	@HAPAttribute
	public static String FROMVALUESTRUCTUREID = "fromValueStructureId";
	@HAPAttribute
	public static String FROMITEMPATH = "fromItemPath";

	@HAPAttribute
	public static String FROMCONSTANT = "fromConstant";
	
	@HAPAttribute
	public static String MATCHERS = "matchers";

	@HAPAttribute
	public static String TODOMAINNAME = "toDomainName";
	@HAPAttribute
	public static String TOVALUESTRUCTUREID = "toValueStructureId";
	@HAPAttribute
	public static String TOITEMPATH = "toItemPath";

	private String m_fromDomainName;
	private String m_fromValueStructureId;
	private String m_fromItemPath;
	
	private Object m_fromConstant;
	
	private HAPMatchers m_matchers;
	
	private String m_toDomainName;
	private String m_toValueStructureId;
	private String m_toItemPath;
	
	public HAPPathValueMapping(String fromDomainName, String fromValueStructureId, String fromItemPath, HAPMatchers matchers, String toDomainName, String toValueStructureId, String toItemPath) {
		this.m_fromDomainName = fromDomainName;
		this.m_fromValueStructureId = fromValueStructureId;
		this.m_fromItemPath = fromItemPath;
		this.m_matchers = matchers;
		this.m_toDomainName = toDomainName;
		this.m_toValueStructureId = toValueStructureId;
		this.m_toItemPath = toItemPath;
	}

	public HAPPathValueMapping(Object fromConstant, HAPMatchers matchers, String toDomainName, String toValueStructureId, String toItemPath) {
		this.m_fromConstant = fromConstant;
		this.m_matchers = matchers;
		this.m_toDomainName = toDomainName;
		this.m_toValueStructureId = toValueStructureId;
		this.m_toItemPath = toItemPath;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FROMCONSTANT, HAPUtilityJson.buildJson(m_fromConstant, HAPSerializationFormat.JSON));
		jsonMap.put(FROMDOMAINNAME, this.m_fromDomainName);
		jsonMap.put(FROMVALUESTRUCTUREID, m_fromValueStructureId);
		jsonMap.put(FROMITEMPATH, this.m_fromItemPath);
		jsonMap.put(MATCHERS, HAPUtilityJson.buildJson(m_matchers, HAPSerializationFormat.JSON));
		jsonMap.put(TODOMAINNAME, this.m_toDomainName);
		jsonMap.put(TOVALUESTRUCTUREID, m_toValueStructureId);
		jsonMap.put(TOITEMPATH, this.m_toItemPath);
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceIdSimple> ids = new ArrayList<HAPResourceIdSimple>();
		ids.addAll(HAPMatcherUtility.getMatchersResourceId(m_matchers));
		dependency.addAll(HAPUtilityResourceId.buildResourceDependentFromResourceId(ids));
	}
}
