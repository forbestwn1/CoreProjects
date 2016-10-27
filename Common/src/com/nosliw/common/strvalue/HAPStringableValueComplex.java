package com.nosliw.common.strvalue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;

public abstract class HAPStringableValueComplex<T extends HAPStringableValue> extends HAPStringableValue{

	public HAPStringableValueComplex(){
	}
	
	public abstract Iterator<T> iterate();
	
	public void init(){}
	
	@Override
	public boolean isEmpty(){
		Iterator<T> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			if(value!=null && !value.isEmpty())  return false;
		}
		return true;
	}
	
	@Override
	protected HAPInterpolateOutput resolveValueByPattern(Map<String, Object> patternDatas) {
		Iterator<T> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			value.resolveByPattern(patternDatas);
		}
		return null;
	}

	@Override
	protected HAPInterpolateOutput resolveValueByInterpolateProcessor(
			Map<HAPInterpolateProcessor, Object> patternDatas) {
		Iterator<T> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			value.resolveByInterpolateProcessor(patternDatas);
		}
		return null;
	}

	@Override
	public boolean isResolved() {
		Iterator<T> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			if(!value.isResolved())  return false;
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
	
	public List<String> getBasicAncestorValueArray(String path){
		HAPStringableValueBasic value = this.getBasicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getListValue();
	}
	
	protected HAPStringableValueEntity getEntityAncestorByPath(String path){ return (HAPStringableValueEntity)this.getAncestorByPath(path); }

	protected HAPStringableValueObject getObjectAncestorByPath(String path){ return (HAPStringableValueObject)this.getAncestorByPath(path); }
	
	protected HAPStringableValueList getListAncestorByPath(String path){ return (HAPStringableValueList)this.getAncestorByPath(path); }
	
	protected HAPStringableValueMap getMapAncestorByPath(String path){ return (HAPStringableValueMap)this.getAncestorByPath(path); }

	protected HAPStringableValueBasic getBasicAncestorByPath(String path){  return (HAPStringableValueBasic)this.getAncestorByPath(path); }
	
	

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
