package com.nosliw.common.info;

import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;

/**
 * Store all the information for data type definition
 * for instance, description, ... 
 */
public interface HAPInfo extends HAPSerializable{

	String getValue(String name);
	
	void setValue(String name, String value);

	Set<String> getNames();
	
}
