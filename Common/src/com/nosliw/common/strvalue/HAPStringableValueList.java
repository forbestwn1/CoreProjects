package com.nosliw.common.strvalue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoList;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPStringableValueList<T> extends HAPStringableValueComplex{

	private List<HAPStringableValue> m_elements;
	
	private boolean m_isAtomicChild = false;
	
	public HAPStringableValueList(){
		this.m_elements = new ArrayList<HAPStringableValue>();
	}

	public HAPStringableValueList(HAPValueInfoList valueInfoList){
		super(valueInfoList);
		this.m_elements = new ArrayList<HAPStringableValue>();
	}
	
	public HAPValueInfoList getValueInfoList(){  return (HAPValueInfoList)this.getValueInfo();  }
	
	public int size(){		return this.m_elements.size();	}
	public HAPStringableValue get(int index){  return this.m_elements.get(index); }
	
	public T getValue(int index){
		if(this.m_isAtomicChild){
			return (T)((HAPStringableValueAtomic)this.m_elements.get(index)).getValue();
		}
		else{
			return (T)this.m_elements.get(index);  
		}
	}
	
	public List<T> getListValue(){  
		if(this.m_isAtomicChild){
			List out = new ArrayList<T>();
			for(HAPStringableValue value : this.m_elements){
				out.add((T)((HAPStringableValueAtomic)value).getValue());
			}
			return out;
		}
		else{
			return (List<T>)this.m_elements;  
		}
	}
	
	public HAPStringableValue addChild(HAPStringableValue element){
		if(element instanceof HAPStringableValueAtomic)   this.m_isAtomicChild = true;
		this.m_elements.add(element);
		return element;
	}
	
	@Override
	public Iterator<HAPStringableValue> iterate(){		return this.m_elements.iterator();	}
	
	@Override
	public String getStringableStructure(){		return HAPConstant.STRINGABLE_VALUESTRUCTURE_LIST;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPStringableValueComplex.ELEMENTS, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON_FULL));
	}
	
	@Override
	protected String buildJson(){
		return HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON);
	}
	
	@Override
	public HAPStringableValue getChild(String name) {
		return m_elements.get(Integer.valueOf(name));
	}

	@Override
	public HAPStringableValue cloneStringableValue() {
		HAPStringableValueList<T> out = new HAPStringableValueList<T>();
		out.cloneFrom(this);
		return out;
	}

	public void clear(){  this.m_elements.clear();  }
	
	protected void cloneFrom(HAPStringableValueList<T> list){
		for(HAPStringableValue element : list.m_elements){
			this.m_elements.add((HAPStringableValue)element.clone());
		}
		this.m_isAtomicChild = list.m_isAtomicChild;
	}

	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPStringableValueList){
			HAPStringableValueList value = (HAPStringableValueList)obj;
			out = HAPBasicUtility.isEqualLists(value.m_elements, value.m_elements);
		}
		return out;
	}
}
