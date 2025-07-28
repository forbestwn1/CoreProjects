package com.nosliw.core.data.criteria;

import java.util.Set;

/**
 * SubCriteria refer to addition criteria information besides the DataTypeCriteria itself
 * Sub Criteria is refered by name
 * It used mostly by complex criteria for instance array and map. They need SubCriteria to describe the criteria for its elements
 */
public interface HAPDataTypeSubCriteriaGroup {

	final public static String ANY = "*"; 
	
	/**
	 * Get criteria by name
	 * @param name
	 * @return  null: does not exits
	 */
	HAPDataTypeCriteria getSubCriteria(String name);

	/**
	 * Get all name for sub criteria
	 * @return 
	 * 		null or empty: no names
	 */
	Set<String> getSubCriteriaNames();

	/**
	 * Only defined sub criteria name, 
	 * Not include all if is open
	 * @return
	 */
	Set<String> getDefinedSubCriteriaNames();
	
	/**
	 * 
	 * @param name
	 * @param child
	 */
	void addSubCriteria(String name, HAPDataTypeCriteria child);
	
	/**
	 * Whether open for other name
	 * @return
	 */
	boolean isOpen();

	
}
