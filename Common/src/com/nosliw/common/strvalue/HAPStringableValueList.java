package com.nosliw.common.strvalue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPStringableValueList<T extends HAPStringableValue> extends HAPStringableValueComplex<T>{

	private List<T> m_elements;
	
	public HAPStringableValueList(){
		this.m_elements = new ArrayList<T>();
	}
	
	public List<T> getListValue(){  return this.m_elements;  }
	
	public HAPStringableValue addChild(T element){
		this.m_elements.add(element);
		return element;
	}
	
	@Override
	public Iterator<T> iterate(){		return this.m_elements.iterator();	}
	
	@Override
	public String getStringableStructure(){		return HAPConstant.STRINGABLE_VALUESTRUCTURE_LIST;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HAPAttributeConstant.STRINGABLEVALUE_ELEMENTS, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON_FULL));
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

	protected void cloneFrom(HAPStringableValueList<T> list){
		for(T element : list.m_elements){
			this.m_elements.add(element);
		}
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
