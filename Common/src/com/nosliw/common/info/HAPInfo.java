package com.nosliw.common.info;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;

/**
 * Store all the information for data type definition
 * for instance, description, ... 
 */
public interface HAPInfo extends HAPSerializable{

	Object getValue(String name);
	
	void setValue(String name, Object value);

	Set<String> getNames();
	
}
