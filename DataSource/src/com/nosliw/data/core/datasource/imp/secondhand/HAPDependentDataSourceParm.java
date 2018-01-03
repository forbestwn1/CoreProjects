package com.nosliw.data.core.datasource.imp.secondhand;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;

public class HAPDependentDataSourceParm extends HAPSerializableImp{

	private String m_mappedVariableName;
	
	private HAPData m_parmData;
	
	public HAPDependentDataSourceParm(Object parmContent){
		this.buildObjectByJson(parmContent);
	}

	public String getMappedVariableName(){  return this.m_mappedVariableName;  }
	public HAPData getParmData(){  return this.m_parmData;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			if(json instanceof String){
				this.m_mappedVariableName = (String)json;
			}
			else if(json instanceof JSONObject){
				this.m_parmData = HAPDataUtility.buildDataWrapperFromJson((JSONObject)json);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
}
