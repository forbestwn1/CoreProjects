package com.nosliw.common.strvalue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPInterpolateProcessor;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public abstract class HAPStringableValueComplex<T extends HAPStringableValue> extends HAPStringableValue{

	@HAPAttribute
	public static String ELEMENTS = "elements";
	
	public HAPStringableValueComplex(){	}

	public HAPStringableValueComplex(HAPValueInfo valueInfo){ super(valueInfo);	}
	
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
	protected HAPInterpolateOutput resolveValueByPattern(Map<String, Object> patternDatas) {
		Iterator<HAPStringableValue> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			value.resolveByPattern(patternDatas);
		}
		return null;
	}

	@Override
	protected HAPInterpolateOutput resolveValueByInterpolateProcessor(
			Map<HAPInterpolateProcessor, Object> patternDatas) {
		Iterator<HAPStringableValue> iterator = this.iterate();
		while(iterator.hasNext()){
			HAPStringableValue value = iterator.next();
			value.resolveByInterpolateProcessor(patternDatas);
		}
		return null;
	}

	@Override
	public boolean isResolved() {
		Iterator<HAPStringableValue> iterator = this.iterate();
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
	
	public <K> List<K> getAtomicAncestorValueArray(String path, String subTypeName){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getListValue(subTypeName);
	}

	public <K> K getAtomicAncestorValueObject(String path, Class<K> cs){
		HAPStringableValueAtomic value = this.getAtomicAncestorByPath(path);
		if(value==null)  return null;
		else	return value.getObjectValue(cs);
	}
	
	protected HAPStringableValueEntity getEntityAncestorByPath(String path){ return (HAPStringableValueEntity)this.getAncestorByPath(path); }

	protected HAPStringableValueObject getObjectAncestorByPath(String path){ return (HAPStringableValueObject)this.getAncestorByPath(path); }
	
	protected HAPStringableValueList getListAncestorByPath(String path){ return (HAPStringableValueList)this.getAncestorByPath(path); }
	
	protected HAPStringableValueMap getMapAncestorByPath(String path){ return (HAPStringableValueMap)this.getAncestorByPath(path); }
	protected Map getMapValueAncestorByPath(String path){
		HAPStringableValueMap map = (HAPStringableValueMap)this.getAncestorByPath(path);
		if(map==null)   return null;
		else return map.getMapValue(); 
	}

	protected HAPStringableValueAtomic getAtomicAncestorByPath(String path){  return (HAPStringableValueAtomic)this.getAncestorByPath(path); }
	
	protected Object getAtomicValueAncestorByPath(String path){
		HAPStringableValueAtomic atomicValue = this.getAtomicAncestorByPath(path);
		if(atomicValue==null)  return null;
		else return atomicValue.getValue();
	}

	public HAPStringableValue getAncestorByPath(String path){
		HAPStringableValue out = this;
		if(HAPBasicUtility.isStringNotEmpty(path)){
			String[] pathSegs = HAPUtilityNamingConversion.parsePaths(path);
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
