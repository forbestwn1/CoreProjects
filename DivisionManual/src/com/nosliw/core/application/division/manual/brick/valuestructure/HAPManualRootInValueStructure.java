package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.structure.HAPElementStructure;

public class HAPManualRootInValueStructure extends HAPEntityInfoWritableImp{

	public static String DEFINITION = "definition";
	
	//context element definition
	private HAPElementStructure m_definition;


	public void setDefinition(HAPElementStructure definition) {   this.m_definition = definition;  }
	public HAPElementStructure getDefinition() {    return this.m_definition;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
	}


	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPManualRootInValueStructure) {
			HAPManualRootInValueStructure root = (HAPManualRootInValueStructure)obj;
			if(!super.equals(obj)) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(this.m_definition, root.m_definition)) {
				return false;
			}
			out = true;
		}
		return out;
	}
}
