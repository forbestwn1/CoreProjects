package com.nosliw.core.application;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.resource.HAPResourceDataImp;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPResourceDataBrick extends HAPResourceDataImp {

	@HAPAttribute
	public final static String BRICK = "brick"; 

	@HAPAttribute
	public final static String SUPPORTBRICKS = "supportBricks"; 

	@HAPAttribute
	public final static String ALIASMAPPING = "aliasMapping"; 

	@HAPAttribute
	public static final String VALUESTRUCTUREDOMAIN = "valueStructureDomain";

	@HAPAttribute
	public final static String EXPORTBRICKINFO = "exportBrickInfo"; 

	private HAPBrick m_brick;
	
	private Map<String, HAPBrick> m_supportBricks;
	
	private Map<String, HAPPath> m_aliasMapping;
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;
	
	private HAPInfoExportBrick m_exportBrickInfo;
	
	public HAPResourceDataBrick(HAPBrick brick, Map<String, HAPBrick> supportBricks, HAPInfoExportBrick exportBrickInfo, Map<String, HAPPath> aliasMapping, HAPDomainValueStructure valueStructureDomain) {
		this.m_supportBricks = new LinkedHashMap<String, HAPBrick>();
		this.m_supportBricks.putAll(supportBricks);
		this.m_brick = brick;
		this.m_exportBrickInfo = exportBrickInfo;
		this.m_valueStructureDomain = valueStructureDomain;
		this.m_aliasMapping = aliasMapping;
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
		jsonMap.put(ALIASMAPPING, HAPUtilityJson.buildJsonStringValue(this.m_aliasMapping, HAPSerializationFormat.JSON));
		jsonMap.put(EXPORTBRICKINFO, this.m_exportBrickInfo.toStringValue(HAPSerializationFormat.JSON));
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
		jsonMap.put(ALIASMAPPING, HAPUtilityJson.buildJsonStringValue(this.m_aliasMapping, HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(EXPORTBRICKINFO, this.m_exportBrickInfo.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_brick.buildResourceDependency(dependency, runtimeInfo);
	}

}
