package com.nosliw.core.application.brick.container;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPUtilityNosliw;

public class HAPBrickContainerList extends HAPBrickContainer{

	@HAPAttribute
	static final public String LIST = HAPUtilityNosliw.buildNosliwFullName("list");  
	
	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(LIST, new ArrayList<String>());
	}

	@Override
	public void addElement(String attrName, HAPEntityOrReference brickOrRef) {   
		super.addElement(attrName, brickOrRef);
		this.addList(attrName);
	}

	@Override
	public String addElement(HAPEntityOrReference brickOrRef) {
		String attrName = super.addElement(brickOrRef);
		this.addList(attrName);
		return attrName;
	}

	private void addList(String attr) {
		List<String> lists = (List<String>)this.getAttributeValueOfValue(LIST);
		lists.add(attr);
	}
	
}
