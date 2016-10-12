package com.nosliw.common.strvalue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.interpolate.HAPInterpolateOutput;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPStringableValueList extends HAPStringableValueComplex{

	private List<HAPStringableValue> m_elements;
	
	public HAPStringableValueList(){
		this.m_elements = new ArrayList<HAPStringableValue>();
	}
	
	public HAPStringableValue addChild(HAPStringableValue element){
		this.m_elements.add(element);
		return element;
	}
	
	@Override
	public Iterator<HAPStringableValue> iterate(){		return this.m_elements.iterator();	}
	
	@Override
	public String getStringableCategary(){		return HAPConstant.STRINGABLE_VALUECATEGARY_LIST;	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format) {
		super.buildFullJsonMap(jsonMap, typeJsonMap, format);
		jsonMap.put(HAPAttributeConstant.STRINGABLEVALUE_ELEMENTS, HAPJsonUtility.getListObjectJson(this.m_elements, format));
	}
	
	@Override
	protected String buildJson(String format){
		return HAPJsonUtility.getListObjectJson(this.m_elements, format);
	}
	

	@Override
	public HAPStringableValue getChild(String name) {
		return m_elements.get(Integer.valueOf(name));
	}

	@Override
	public HAPStringableValue cloneStringableValue() {
		HAPStringableValueList out = new HAPStringableValueList();
		out.cloneFrom(this);
		return out;
	}

	protected void cloneFrom(HAPStringableValueList list){
		for(HAPStringableValue element : list.m_elements){
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
