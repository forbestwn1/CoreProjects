package com.nosliw.data;

import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPStringable;

/*
 * class that store data
 *  
 */
public interface HAPData extends HAPStringable{
	
	/*
	 * get data type object
	 */
	public HAPDataType getDataType();
	
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
	
	/*
	 * run operation on this data
	 */
	public HAPServiceData operate(String operation, List<HAPData> parms, HAPOperationContext opContext);

}
