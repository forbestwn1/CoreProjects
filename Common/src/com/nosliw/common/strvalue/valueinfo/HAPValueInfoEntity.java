package com.nosliw.common.strvalue.valueinfo;

import java.util.Set;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoEntity extends HAPValueInfoComplex{

	public static final String ATTR_CLASSNAME = "class";
	public static final String ATTR_MANDATORY = "mandatory";
	public static final String ATTR_PROPERTIES = "property";
	public static final String ATTR_PARENT = "parent";
	
	@Override
	public String getCategary() {		return HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY;	}

	@Override
	public void init(){
		super.init();
		this.updateBasicChildValue(ATTR_MANDATORY, true);
	}
	
	@Override
	public HAPValueInfo getElement(String name){
		HAPStringableValueEntity properties = this.getPropertiesEntity();
		return (HAPValueInfo)properties.getChild(name);
	}

	public Set<String> getProperties(){
		HAPStringableValueEntity properties = this.getPropertiesEntity();
		return properties.getProperties();
	}
	
	private HAPStringableValueEntity getPropertiesEntity(){		return (HAPStringableValueEntity)this.getChild(ATTR_PROPERTIES);	}

}
