package com.nosliw.common.strvalue.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	public String getStringableCategary(){		return HAPConstant.CONS_STRINGABLE_VALUECATEGARY_LIST;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put("elements", HAPJsonUtility.getListObjectJson(this.m_elements));
	}

	@Override
	public HAPStringableValue getChild(String name) {
		return m_elements.get(Integer.valueOf(name));
	}

	@Override
	public HAPStringableValue clone() {
		HAPStringableValueList out = new HAPStringableValueList();
		out.cloneFrom(this);
		return out;
	}

	public void cloneFrom(HAPStringableValueList list){
		for(HAPStringableValue element : list.m_elements){
			this.m_elements.add(element);
		}
	}
}
