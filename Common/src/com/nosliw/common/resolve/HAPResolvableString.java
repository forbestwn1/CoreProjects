package com.nosliw.common.resolve;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateProcessorByConfigureForDoc;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.interpolate.HAPInterpolateOutput;

/*
 * string can be resolvable
 * it can be used to store string in configure, xml file
 */
public class HAPResolvableString implements HAPResolvable{

	private String m_value;

	private String m_resolvedValue;
	
	private boolean m_resolved = false;
	
	public HAPResolvableString(String value){
		this(value, false);
	}

	public HAPResolvableString(String value, boolean resolved){
		this.setValue(value);
		this.m_resolved = resolved;
	}
	
	private HAPResolvableString(){}
	
	public boolean isEmpty(){ return this.m_value==null;  }
	
	public String getValue(){		return this.m_resolvedValue;	}
	public void setValue(String value){
		this.m_value = value;
		this.m_resolvedValue = this.m_value;
	}
	
	@Override
	public boolean isResolved(){  return this.m_resolved; }
	
	public HAPInterpolateOutput resolveByConfigure(HAPConfigureImp configure) {
		Map<HAPInterpolateProcessor, Object> interpolateDatas = new LinkedHashMap<HAPInterpolateProcessor, Object>();
		interpolateDatas.put(new HAPInterpolateProcessorByConfigureForDoc(), configure);
		return this.resolveByInterpolateProcessor(interpolateDatas);
	}
	
	/*
	 * update interpolate for all string values
	 * patternDatas : pattern name --- pattern process data 
	 */
	@Override
	public HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas){
		HAPInterpolateOutput out = null;
		if(patternDatas!=null){
			out = HAPResolvableUtility.resolveByPatterns(this.m_value, patternDatas);
			this.m_resolvedValue = out.getOutput();
			this.m_resolved = out.isResolved();
		}
		else{
			out = new HAPInterpolateOutput();
			out.setOutput(this.m_value);
			this.m_resolvedValue = this.m_value;
			this.m_resolved = true;
		}
		return out;
	}
	
	@Override
	public HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateProcessor, Object> interpolateDatas){
		HAPInterpolateOutput out = null;
		if(interpolateDatas!=null){
			out = HAPResolvableUtility.resolveByInterpolateProcessors(this.m_value, interpolateDatas);
			this.m_resolvedValue = out.getOutput();
			this.m_resolved = out.isResolved();
		}
		else{
			out = new HAPInterpolateOutput();
			out.setOutput(this.m_value);
			this.m_resolvedValue = this.m_value;
			this.m_resolved = true;
		}
		return out;
	}
	

	@Override
	public String toString(){		return this.getValue();	}
	
	@Override
	public boolean equals(Object data){
		boolean out = false;
		if(data instanceof HAPResolvableString){
			HAPResolvableString obj = (HAPResolvableString)data;
			out = HAPUtilityBasic.isEquals(obj.getValue(), this.getValue());
		}
		return out;
	}
	
	public HAPResolvableString clone(){
		HAPResolvableString out = new HAPResolvableString();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResolvableString resolvableValue){
		this.m_value = resolvableValue.m_value;
		this.m_resolvedValue = resolvableValue.m_resolvedValue;
		this.m_resolved = resolvableValue.m_resolved;
	}
}
