package com.nosliw.core.application.common.structure;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.dataassociation.HAPInfoElementRuntime;

@HAPEntityWithAttribute
public class HAPElementStructureLeafRuntime extends HAPElementStructure{

	@HAPAttribute
	public static final String RUNTIME = "runtime";

	@HAPAttribute
	public static final String HANDLER = "handler";

	private List<HAPInfoElementRuntime> m_runtime;
	
	private String m_handler;
	
	public HAPElementStructureLeafRuntime() {	}

	@Override
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_RUNTIME;	}

	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafRuntime out = new HAPElementStructureLeafRuntime();
		this.toStructureElement(out);
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(VALUE, this.m_value.toString());
		typeJsonMap.put(VALUE, this.m_value.getClass());
	}

	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
		((HAPElementStructureLeafRuntime)out).m_value = this.m_value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafRuntime) {
			HAPElementStructureLeafRuntime ele = (HAPElementStructureLeafRuntime)obj;
			if(!HAPUtilityBasic.isEquals(this.m_value, ele.m_value)) {
				return false;
			}
			out = true;
		}
		return out;
	}

}
