package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * store variable information based on 
 * 		context name
 * 		path
 */
public class HAPContextVariable extends HAPSerializableImp{

	//context name
	private String m_name;
	//value path
	private String m_path;
	
	public HAPContextVariable(String expression){
		this.m_name = expression;
		this.m_path = "";
		int index = expression.indexOf(HAPConstant.SEPERATOR_PATH);
		if(index!=-1){
			this.m_name = expression.substring(0, index);
			this.m_path = expression.substring(index+1);
		}
	}
	
	public HAPContextVariable(String name, String path){
		this.m_name = name;
		this.m_path = path;
	}
	
	public String getName(){return this.m_name;}
	public String getPath(){return this.m_path;}

	public boolean equals(Object o){
		if(o instanceof HAPContextVariable){
			HAPContextVariable k = (HAPContextVariable)o;
			if(HAPBasicUtility.isEquals(k.getName(), this.getName()) && HAPBasicUtility.isEquals(k.getPath(), this.getPath())){
				return true;
			}
			else return false;
		}
		else return false;
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(HAPAttributeConstant.CONTEXTVARIABLE_NAME, this.m_name);
		jsonMap.put(HAPAttributeConstant.CONTEXTVARIABLE_PATH, this.m_path);
	}

}
