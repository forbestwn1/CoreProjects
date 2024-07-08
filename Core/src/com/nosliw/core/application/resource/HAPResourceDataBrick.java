package com.nosliw.core.application.resource;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.executable.HAPBrick;
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
	public static final String VALUESTRUCTUREDOMAIN = "valueStructureDomain";

	private HAPBrick m_brick;
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPResourceDataBrick(HAPBrick brick) {
		this.m_brick = brick;
	}
	
	public HAPResourceDataBrick(HAPBrick brick, HAPDomainValueStructure valueStructureDomain) {
		this(brick);
		this.m_valueStructureDomain = valueStructureDomain;
	}
	
	public HAPBrick getBrick() {    return this.m_brick;     }
	
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
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brick.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		if(this.m_valueStructureDomain!=null) {
			jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_brick.buildResourceDependency(dependency, runtimeInfo);
	}

}
