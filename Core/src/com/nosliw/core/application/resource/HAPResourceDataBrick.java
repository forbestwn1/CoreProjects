package com.nosliw.core.application.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPResourceDataImp;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPResourceDataBrick extends HAPResourceDataImp {

	@HAPAttribute
	public final static String BRICK = "brick"; 

	@HAPAttribute
	public final static String SUPPORTBRICKS = "supportBricks"; 

	@HAPAttribute
	public static final String VALUESTRUCTUREDOMAIN = "valueStructureDomain";

	private HAPBrick m_brick;
	
	private Map<String, HAPBrick> m_supportBricks;
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPResourceDataBrick(HAPBrick brick, Map<String, HAPBrick> supportBricks) {
		this.m_supportBricks = new LinkedHashMap<String, HAPBrick>();
		this.m_supportBricks.putAll(supportBricks);
		this.m_brick = brick;
	}
	
	public HAPResourceDataBrick(HAPBrick brick, Map<String, HAPBrick> supportBricks, HAPDomainValueStructure valueStructureDomain) {
		this(brick, supportBricks);
		this.m_valueStructureDomain = valueStructureDomain;
	}
	
	public HAPBrick getBrick() {    return this.m_brick;     }
	
	public HAPDomainValueStructure getValueStructureDomain() {   return this.m_valueStructureDomain;    }
	
	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {
		throw new RuntimeException();
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_valueStructureDomain!=null) {
			jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toStringValue(HAPSerializationFormat.JSON));
		}
		
		Map<String, String> supportBrickMap = new LinkedHashMap<String, String>();
		for(String name : this.m_supportBricks.keySet()) {
			supportBrickMap.put(name, this.m_supportBricks.get(name).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(SUPPORTBRICKS, HAPUtilityJson.buildMapJson(supportBrickMap));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		if(this.m_valueStructureDomain!=null) {
			jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}

		Map<String, String> supportBrickMap = new LinkedHashMap<String, String>();
		for(String name : this.m_supportBricks.keySet()) {
			supportBrickMap.put(name, this.m_supportBricks.get(name).toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(SUPPORTBRICKS, HAPUtilityJson.buildMapJson(supportBrickMap));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_brick.buildResourceDependency(dependency, runtimeInfo);
	}

}
