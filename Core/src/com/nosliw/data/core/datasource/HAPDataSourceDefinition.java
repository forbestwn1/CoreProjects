package com.nosliw.data.core.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.criteria.HAPCriteriaParser;

@HAPEntityWithAttribute
public class HAPDataSourceDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String PARMS = "parms";
	
	@HAPAttribute
	public static String OUTPUT = "output";
	
	@HAPAttribute
	public static String CONFIGURE = "configure";
	
	private String m_name;
	
	private String m_description;
	
	private Map<String, HAPDataSourceParm> m_parms;
	
	private HAPDataSourceOutput m_output;
	
	private Object m_configure;
	
	private HAPCriteriaParser m_parser;
	
	public String getName(){  return this.m_name;   }
	
	public String getDescription(){   return this.m_description;   }
	
	public Object getConfigure(){  return this.m_configure;   }

	public Map<String, HAPDataSourceParm> getParms(){  return this.m_parms;   }
	
	public HAPDataSourceOutput getOutput(){ return this.m_output;  }
	
	public HAPDataSourceDefinition(){
		this.m_parms = new LinkedHashMap<String, HAPDataSourceParm>();
		this.m_parser = HAPCriteriaParser.getInstance();
	}
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_name = objJson.getString(NAME);
			this.m_description = objJson.optString(DESCRIPTION);
			this.m_output = new HAPDataSourceOutput(this.m_parser.parseCriteria(objJson.getString(OUTPUT)));
			
			JSONArray parmsArray = objJson.getJSONArray(PARMS);
			for(int i = 0; i<parmsArray.length(); i++){
				HAPDataSourceParm parm = new HAPDataSourceParm();
				parm.buildObject(parmsArray.get(i), HAPSerializationFormat.JSON);
				this.m_parms.put(parm.getName(), parm);
			}
			
			this.m_configure = objJson.opt(CONFIGURE);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;  
	}
}
