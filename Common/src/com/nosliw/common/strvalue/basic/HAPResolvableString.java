package com.nosliw.common.strvalue.basic;

import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateExpressionProcessor;
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
	public boolean isStringResolved(){  return this.m_resolved; }
	
	/*
	 * update interpolate for all string values
	 * patternDatas : pattern name --- pattern process data 
	 */
	@Override
	public HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas){
		HAPInterpolateOutput out = null;
		if(patternDatas!=null){
			out = HAPStringResolveUtility.resolveByPatterns(this.m_value, patternDatas);
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
	public HAPInterpolateOutput resolveByInterpolateProcessor(Map<HAPInterpolateExpressionProcessor, Object> interpolateDatas){
		HAPInterpolateOutput out = null;
		if(interpolateDatas!=null){
			out = HAPStringResolveUtility.resolveByInterpolateProcessors(this.m_value, interpolateDatas);
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
