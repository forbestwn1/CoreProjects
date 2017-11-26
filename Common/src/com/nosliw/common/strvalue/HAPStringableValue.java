package com.nosliw.common.strvalue;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateProcessorByConfigureForDoc;
import com.nosliw.common.resolve.HAPResolvable;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;

@HAPEntityWithAttribute(baseName="STRINGABLEVALUE")
public abstract class HAPStringableValue extends HAPSerializableImp implements HAPResolvable{

	//The value info for this value
	private HAPValueInfo m_valueInfo;
	
	public HAPStringableValue(){}
	
	public HAPStringableValue(HAPValueInfo valueInfo){
		this.m_valueInfo = valueInfo;
	}
	
	public HAPValueInfo getValueInfo(){ return this.m_valueInfo;  }
	public void setValueInfo(HAPValueInfo valueInfo){  this.m_valueInfo = valueInfo;  }
	
	@HAPAttribute
	public static String STRUCTURE = "structure";
	
	public abstract String getStringableStructure();

	public abstract HAPStringableValue getChild(String name);

	public abstract boolean isEmpty();

	public void afterBuild(){}

	public void afterResolve(HAPInterpolateOutput resolveResult){}
	
	protected abstract HAPStringableValue cloneStringableValue();
	
	public HAPStringableValue clone(){
		HAPStringableValue out = this.cloneStringableValue();
		out.afterBuild();
		return out;
	}

	@Override
	final public HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas){
		HAPInterpolateOutput out = this.resolveValueByPattern(patternDatas);
		this.afterResolve(out);
		return out;
	}
	@Override
	final public HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateProcessor, Object> patternDatas){
		HAPInterpolateOutput out = this.resolveValueByInterpolateProcessor(patternDatas);
		this.afterResolve(out);
		return out;
	}
	
	protected abstract HAPInterpolateOutput resolveValueByPattern(Map<String, Object> patternDatas);
	protected abstract HAPInterpolateOutput resolveValueByInterpolateProcessor(Map<HAPInterpolateProcessor, Object> patternDatas);
	
	
	public HAPInterpolateOutput resolveByConfigure(HAPConfigureImp configure) {
		Map<HAPInterpolateProcessor, Object> interpolateDatas = new LinkedHashMap<HAPInterpolateProcessor, Object>();
		if(configure!=null)		interpolateDatas.put(new HAPInterpolateProcessorByConfigureForDoc(), configure);
		HAPInterpolateOutput out = this.resolveByInterpolateProcessor(interpolateDatas);
		return out;
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STRUCTURE, this.getStringableStructure());
	}
	
	@Override
	protected String buildLiterate(){  return this.toStringValue(HAPSerializationFormat.JSON); }

}
