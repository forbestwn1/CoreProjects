package com.nosliw.entity.sort;

import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;

public abstract class HAPSortingInfo {

	private String m_type;
	
	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPSortingInfo(String type, HAPDataTypeManager dataTypeMan){
		this.m_type = type;
		this.m_dataTypeMan = dataTypeMan;
	}

	/*
	 * abstract method need sub class implementation
	 * 
	 */
	abstract public int compare(HAPWraper data1, HAPWraper data2);

	public String getType(){return this.m_type;}
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
}
