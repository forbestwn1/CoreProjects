package com.nosliw.data;

import com.nosliw.common.serialization.HAPSerializable;

/*
 * class that store data
 *  
 */
public interface HAPData extends HAPSerializable{
	
	/**
	 * get data type object
	 */
	public HAPDataTypeInfo getDataTypeInfo();
	
	/**
	 * get value within data
	 */
	public Object getValue();
	
}
