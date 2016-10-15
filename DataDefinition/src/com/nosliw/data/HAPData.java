package com.nosliw.data;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.datatype.HAPDataTypeInfo;

/*
 * class that store data
 *  
 */
public interface HAPData extends HAPSerializable{
	
	/*
	 * get data type object
	 */
	public HAPDataTypeInfo getDataTypeInfo();
	
	/*
	 * check if the data is empty data
	 * whether data is empty or not is totally depended on type of data, case by case 
	 */
	public boolean isEmpty();
	
	/*
	 * clone another data
	 */
	public HAPData cloneData();

	/*
	 * clear up resource method before this data is destroyed
	 * inputs:
	 * 		parms   we can change the behavior of clear up through parmameters 
	 */
	public void clearUp(Map<String, Object> parms);
	
}
