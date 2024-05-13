package com.nosliw.core.application.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.structure.HAPPathElementMapping;
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
public class HAPPathValueMapping2 extends HAPExecutableImp{

	@HAPAttribute
	public static String FROMVALUEPORTID = "fromValuePortId";
	@HAPAttribute
	public static String FROMVALUESTRUCTUREID = "fromValueStructureId";
	@HAPAttribute
	public static String FROMITEMPATH = "fromItemPath";

	@HAPAttribute
	public static String FROMCONSTANT = "fromConstant";
	
	@HAPAttribute
	public static String FROMPROVIDENAME = "fromProvideName";
	@HAPAttribute
	public static String FROMPROVIDEPATH = "fromProvidePath";

	@HAPAttribute
	public static String MATCHERS = "matchers";

	@HAPAttribute
	public static String TOVALUEPORTID = "toValuePortId";
	@HAPAttribute
	public static String TOVALUESTRUCTUREID = "toValueStructureId";
	@HAPAttribute
	public static String TOITEMPATH = "toItemPath";

	private HAPReferenceValuePort m_fromValuePortRef;
	private String m_fromValueStructureId;
	private String m_fromItemPath;
	
	private Object m_fromConstant;
	
	private String m_fromProvideName;
	private String m_fromProvidePath;
	
	private List<HAPPathElementMapping> m_fromProvideValueMappingPath;
	
	private HAPMatchers m_matchers;
	
	private HAPReferenceValuePort m_toValuePortRef;
	private String m_toValueStructureId;
	private String m_toItemPath;
	
	public HAPPathValueMapping2(HAPReferenceValuePort fromValuePortId, String fromValueStructureId, String fromItemPath, HAPMatchers matchers, HAPReferenceValuePort toValuePortId, String toValueStructureId, String toItemPath) {
		this.m_fromValuePortRef = fromValuePortId;
		this.m_fromValueStructureId = fromValueStructureId;
		this.m_fromItemPath = fromItemPath;
		this.m_matchers = matchers;
		this.m_toValuePortRef = toValuePortId;
		this.m_toValueStructureId = toValueStructureId;
		this.m_toItemPath = toItemPath;
	}

	public HAPPathValueMapping2(Object fromConstant, HAPMatchers matchers, HAPReferenceValuePort toValuePortId, String toValueStructureId, String toItemPath) {
		this.m_fromConstant = fromConstant;
		this.m_matchers = matchers;
		this.m_toValuePortRef = toValuePortId;
		this.m_toValueStructureId = toValueStructureId;
		this.m_toItemPath = toItemPath;
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
