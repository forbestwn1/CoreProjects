package com.nosliw.common.strvalue.basic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateProcessorByConfigureForDoc;
import com.nosliw.common.serialization.HAPStringableJson;

public abstract class HAPStringableValue extends HAPStringableJson implements HAPResolvable{

	public abstract String getStringableCategary();

	public abstract HAPStringableValue getChild(String name);

	public abstract boolean isEmpty();

	public void afterBuild(){}

	protected abstract HAPStringableValue cloneStringableValue();
	
	public HAPStringableValue clone(){
		HAPStringableValue out = this.cloneStringableValue();
		out.afterBuild();
		return out;
	}
	
	public HAPInterpolateOutput resolveByConfigure(HAPConfigureImp configure) {
		Map<HAPInterpolateProcessor, Object> interpolateDatas = new LinkedHashMap<HAPInterpolateProcessor, Object>();
		interpolateDatas.put(new HAPInterpolateProcessorByConfigureForDoc(), configure);
		return this.resolveByInterpolateProcessor(interpolateDatas);
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format) {
		jsonMap.put(HAPAttributeConstant.STRINGABLEVALUE_CATEGARY, this.getStringableCategary());
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format) {}
}
