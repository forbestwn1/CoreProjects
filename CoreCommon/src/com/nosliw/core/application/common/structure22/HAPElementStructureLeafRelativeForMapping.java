package com.nosliw.core.application.common.structure22;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPElementStructureLeafRelativeForMapping extends HAPElementStructureLeafRelative{

	@HAPAttribute
	public static final String MAPPING = "mapping";
	
	@HAPAttribute
	public static final String MATCHERS = "matchers";

	//context node full name --- matchers
	//used to convert data from parent to data within uiTag
	private HAPMatchers m_matchers;

	public HAPElementStructureLeafRelativeForMapping() {
	}
	
	public HAPElementStructureLeafRelativeForMapping(String path) {
		super(path);
	}
	
	@Override
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING;	}

	public void setMatchers(HAPMatchers matchers){	this.m_matchers = matchers;	}

	public void toStructureElement(HAPElementStructureLeafRelativeForMapping out) {
		super.toStructureElement(out);
		out.m_matchers = this.m_matchers.cloneMatchers();
	}
	
	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafRelativeForMapping out = new HAPElementStructureLeafRelativeForMapping();
		this.toStructureElement(out);
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAPPING, this.getReference().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafRelativeForMapping) {
			HAPElementStructureLeafRelativeForMapping ele = (HAPElementStructureLeafRelativeForMapping)obj;
			if(!HAPUtilityBasic.isEquals(this.getReference(), ele.getReference())) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(ele.m_matchers, this.m_matchers)) {
				return false;
			}
			out = true;
		}
		return out;
	}	
}
