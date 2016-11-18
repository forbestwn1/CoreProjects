package com.nosliw.common.strvalue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;

public abstract class HAPStringableValueComplex<T extends HAPStringableValue> extends HAPStringableValue{

	@HAPAttribute
	public static String ELEMENTS = "elements";
	
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

	public Object getAtomicAncestorValue(String path, String type, String subType){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else		return value.getValue(type, subType);
	}
	
	public Object getAtomicAncestorValue(String path){
		HAPStringableValueAtomic value = getAtomicAncestorByPath(path);
		if(value==null)  return null;
		return value.getValue();
	}
	

	public String getAtomicAncestorValueString(String path){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getStringValue();
	}
	
	public Boolean getAtomicAncestorValueBoolean(String path){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getBooleanValue();
	}
	
	public Integer getAtomicAncestorValueInteger(String path){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getIntegerValue();
	}
	
	public Float getAtomicAncestorValueFloat(String path){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getFloatValue();
	}
	
	public List<T> getAtomicAncestorValueArray(String path, Class<T> cs){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getListValue(cs);
	}
	
	protected HAPStringableValueEntity getEntityAncestorByPath(String path){ return (HAPStringableValueEntity)this.getAncestorByPath(path); }

	protected HAPStringableValueObject getObjectAncestorByPath(String path){ return (HAPStringableValueObject)this.getAncestorByPath(path); }
	
	protected HAPStringableValueList getListAncestorByPath(String path){ return (HAPStringableValueList)this.getAncestorByPath(path); }
	
	protected HAPStringableValueMap getMapAncestorByPath(String path){ return (HAPStringableValueMap)this.getAncestorByPath(path); }

	protected HAPStringableValueAtomic getAtomicAncestorByPath(String path){  return (HAPStringableValueAtomic)this.getAncestorByPath(path); }
	
	

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
