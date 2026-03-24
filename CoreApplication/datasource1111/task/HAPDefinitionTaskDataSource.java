package com.nosliw.data.core.datasource.task;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.task111.HAPDefinitionTask;

public class HAPDefinitionTaskDataSource extends HAPDefinitionTask{

	@HAPAttribute
	public static String DATASOURCE = "dataSource";

	@HAPAttribute
	public static String PARMS = "parms";
	
	private String m_dataSource;
	
	private Map<String, HAPDefinitionExpression> m_parmsDef;
	
	public HAPDefinitionTaskDataSource() {
		this.m_parmsDef = new LinkedHashMap<String, HAPDefinitionExpression>();
	}
	
	public HAPDefinitionTaskDataSource(String dataSource) {
		this();
		this.m_dataSource = dataSource;
	}
	
	@Override
	public String getType() {	return HAPConstant.DATATASK_TYPE_DATASOURCE;	}

	public String getDataSource() {   return this.m_dataSource;    }
	public Map<String, HAPDefinitionExpression> getParmsDef(){   return this.m_parmsDef;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			this.m_dataSource = jsonObj.getString(DATASOURCE);
			
			{
				JSONObject parmsObj = jsonObj.optJSONObject(PARMS);
				if(parmsObj!=null){
					Iterator<String> its = parmsObj.keys();
					while(its.hasNext()){
						String name = its.next();
						String parmDef = parmsObj.getString(name);
						this.m_parmsDef.put(name, new HAPDefinitionExpression(parmDef));
					}
				}
			}
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATASOURCE, this.m_dataSource);
		jsonMap.put(PARMS, HAPJsonUtility.buildJson(this.m_parmsDef, HAPSerializationFormat.JSON));
	}
	
}
