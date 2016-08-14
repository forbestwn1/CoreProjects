package com.nosliw.common.strvalue.basic;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateExpressionProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;

public abstract class HAPStringableValueComplex extends HAPStringableValue{

	public HAPStringableValueComplex(){
	}
	
	public abstract Iterator<HAPStringableValue> iterate();
	
	public void init(){}
	
	@Override
	public boolean isEmpty(){
		Iterator<HAPStringableValue> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			if(value!=null && !value.isEmpty())  return false;
		}
		return true;
	}
	
	@Override
	public HAPInterpolateOutput resolveByPattern(Map<String, Object> patternDatas) {
		Iterator<HAPStringableValue> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			value.resolveByPattern(patternDatas);
		}
		return null;
	}

	@Override
	public HAPInterpolateOutput resolveByInterpolateProcessor(
			Map<HAPInterpolateExpressionProcessor, Object> patternDatas) {
		Iterator<HAPStringableValue> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			value.resolveByInterpolateProcessor(patternDatas);
		}
		return null;
	}

	@Override
	public boolean isStringResolved() {
		Iterator<HAPStringableValue> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			if(!value.isStringResolved())  return false;
		}
		return true;
	}

	public Object getBasicAncestorValue(String path, String type){
		HAPStringableValueBasic value = this.getBasicAncestorByPath(path);
		if(value==null)  return null;
		else		return value.getValue(type);
	}
	
	public Object getBasicAncestorValue(String path){
		HAPStringableValueBasic value = getBasicAncestorByPath(path);
		if(value==null)  return null;
		return value.getValue();
	}
	

	public String getBasicAncestorValueString(String path){
		HAPStringableValueBasic value = this.getBasicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getStringValue();
	}
	
	public Boolean getBasicAncestorValueBoolean(String path){
		HAPStringableValueBasic value = this.getBasicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getBooleanValue();
	}
	
	public Integer getBasicAncestorValueInteger(String path){
		HAPStringableValueBasic value = this.getBasicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getIntegerValue();
	}
	
	public Float getBasicAncestorValueFloat(String path){
		HAPStringableValueBasic value = this.getBasicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getFloatValue();
	}
	
	public List<String> getBasicAncestorValueList(String path){
		HAPStringableValueBasic value = this.getBasicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getListValue();
	}
	
	
	protected HAPStringableValueBasic getBasicAncestorByPath(String path){
		HAPStringableValueBasic out = (HAPStringableValueBasic)this.getAncestorByPath(path);
		return out;
	}

	public HAPStringableValue getAncestorByPath(String path){
		HAPStringableValue out = this;
		if(HAPBasicUtility.isStringNotEmpty(path)){
			String[] pathSegs = HAPNamingConversionUtility.parsePathSegs(path);
			if(pathSegs!=null){
				for(String pathSeg : pathSegs){
					out = out.getChild(pathSeg);
					if(out==null)  break;
				}
			}
		}
		return out;
	}
}
