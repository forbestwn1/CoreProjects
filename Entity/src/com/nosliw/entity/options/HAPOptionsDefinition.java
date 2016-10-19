package com.nosliw.entity.options;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.HAPData;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;

/*
 * options is a limited set of data
 * beside constaint, options can play same role as a constrain for attribute, options can override rule
 * options would not be treated as a data type in system
 * not only provide data list, but also provide the display name for each element
 */

public abstract class HAPOptionsDefinition implements HAPSerializable{
	//unique name for options defintion
	private String m_name;
	private String m_description;
	//data type for options data
	private HAPDataTypeDefInfo m_dataTypeDefInfo;
	
	HAPDataTypeManager m_dataTypeMan;
	
	public HAPOptionsDefinition(String name, HAPDataTypeDefInfo dataTypeDefInfo, String desc, HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
		this.m_name = name;
		this.m_description = desc;
		this.m_dataTypeDefInfo = dataTypeDefInfo;
	}
	
	abstract public String getType();
	
	abstract public void init();
	
	public String getName(){return this.m_name;}
	
	public String getDescription(){	return this.m_description;	}
	
	public HAPDataTypeDefInfo getDataTypeInfo(){return this.m_dataTypeDefInfo;}

	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
	
	protected HAPWraper createOptionDataWraper(HAPData data){return new HAPWraper(data, this.getDataTypeManager());	}
}
