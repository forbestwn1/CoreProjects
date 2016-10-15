package com.nosliw.entity.options;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.entity.data.HAPEntityData;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.utils.HAPEntityUtility;

/*
 *  different type of options:
 *  	common options:  
 *  	attribute options: options value is based on the context
 *  the behavior of options can be configured using option configure
 */

public class HAPOptionsDefinitionManager{

	Map<String, HAPOptionsDefinition> m_defs = null;
	
	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPOptionsDefinitionManager(HAPDataTypeManager dataTypeMan){
		m_defs = new LinkedHashMap<String, HAPOptionsDefinition>();
		this.m_dataTypeMan = dataTypeMan;
		init();
	}

	/*
	 * add options defintion
	 */
	public void registerOptionsDefinition(HAPOptionsDefinition def){
		this.m_defs.put(def.getName(), def);
	}
	
	public void init(){
	}
	
	
	/*
	 * get all options names
	 */
	public String[] getOptionsNames(){
		return this.m_defs.keySet().toArray(new String[0]);
	}
	
	/*
	 * get options defintion by name
	 */
	public HAPOptionsDefinition getOptionsDefinition(String name){
		return this.m_defs.get(name);
	}

	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}

//	public HAPContainerOptions getOptionsContainerDataType(){
//		return (HAPContainerOptions)this.getDataTypeManager().getDataType(new HAPDataTypeInfo(HAPConstant.DATATYPE_CATEGARY_CONTAINER, HAPConstant.DATATYPE_TYPE_CONTAINER_OPTIONS));
//	}
//	
//	public HAPContainerOptionsData createOptionsContainerData(){
//		return (HAPContainerOptionsData)this.getOptionsContainerDataType().getDefaultData();
//	}
	
}
