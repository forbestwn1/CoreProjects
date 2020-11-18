package com.nosliw.common.info;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;

/**
 * Store all the information for data type definition
 * for instance, description, ... 
 */
public interface HAPInfo extends HAPSerializable{

	Object getValue(String name);
	
	Object getValue(String name, Object defaultValue);
	
	Object setValue(String name, Object value);

	Set<String> getNames();

	HAPInfo cloneInfo(Set<String> excluded);

	HAPInfo cloneInfo();
}
