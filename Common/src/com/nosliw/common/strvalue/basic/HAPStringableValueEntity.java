package com.nosliw.common.strvalue.basic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * container for stringable value
 */
public class HAPStringableValueEntity extends HAPStringableValueComplex{

	private Map<String, HAPStringableValue> m_childrens;

	public HAPStringableValueEntity(){
		this.m_childrens = new LinkedHashMap<String, HAPStringableValue>();
	}

	@Override
	public Iterator<HAPStringableValue> iterate(){		return this.m_childrens.values().iterator();	}
	
	@Override
	public String getStringableCategary(){		return HAPConstant.CONS_STRINGABLE_VALUECATEGARY_ENTITY;	}
	
	@Override
	public HAPStringableValue getChild(String name){  return this.m_childrens.get(name);  }
	
	public HAPStringableValue addChild(String name, HAPStringableValue entity){
		this.m_childrens.put(name, entity);
		return entity;
	}

	public HAPStringableValueComplex updateComplexChild(String name, String type){
		HAPStringableValueComplex out = (HAPStringableValueComplex)this.getChild(name);
		if(out==null){
			out = HAPStringResolveUtility.newStringableValueComplex(type);
			if(out!=null){
				this.addChild(name, out);
			}
		}
		return out;
	}
	
	public HAPStringableValueBasic updateBasicChild(String name, String strValue, String type){
		HAPStringableValueBasic out = null; 
		HAPStringableValue child = this.getChild(name);
		if(child==null || child.getStringableCategary().equals(HAPConstant.CONS_STRINGABLE_VALUECATEGARY_BASIC)){
			out = new HAPStringableValueBasic(strValue, type);
			this.m_childrens.put(name, out);
		}
		return out;
	}
	
	public void updateBasicChild(String name, String strValue){
		this.updateBasicChild(name, strValue, null);
	}

	public HAPStringableValueBasic updateBasicChildValue(String name, Object value){
		HAPStringableValueBasic out = null; 
		HAPStringableValue child = this.getChild(name);
		if(child==null || child.getStringableCategary().equals(HAPConstant.CONS_STRINGABLE_VALUECATEGARY_BASIC)){
			out = new HAPStringableValueBasic();
			out.setValue(value);
			this.m_childrens.put(name, out);
		}
		return out;
	}
	
	public void updateBasicChildrens(Map<String, String> values){
		for(String name : values.keySet()){
			this.updateBasicChild(name, values.get(name));
		}
	}
	
	public Set<String> getProperties(){		return this.m_childrens.keySet();	}

	@Override
	public HAPStringableValueEntity clone(){
		HAPStringableValueEntity out = new HAPStringableValueEntity();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPStringableValueEntity entity){
		for(String name : entity.m_childrens.keySet()){
			this.m_childrens.put(name, entity.m_childrens.get(name).clone());
		}
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put("properties", HAPJsonUtility.getMapObjectJson(this.m_childrens));
	}

}
