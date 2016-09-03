package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * store variable information based on 
 * 		context name
 * 		path
 */
public class HAPContextVariable  implements HAPStringable{

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
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.CONTEXTVARIABLE_NAME, this.m_name);
		jsonMap.put(HAPAttributeConstant.CONTEXTVARIABLE_PATH, this.m_path);
		return HAPJsonUtility.getMapJson(jsonMap);
	}
}
