package com.nosliw.data.core.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;

@HAPEntityWithAttribute
public class HAPDefinitionResultActivity extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	//associate variable in process to input required by activity 
	private HAPDefinitionDataAssociation m_output;
	
	public HAPDefinitionDataAssociation getOutputDataAssociation() {   return this.m_output;   }
	
	public void setOutputDataAssociation(HAPDefinitionDataAssociation output) {  this.m_output = output;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			//data association output should not be flat
			this.m_output = HAPParserDataAssociation.buildDefinitionByJson(jsonObj.optJSONObject(OUTPUT));
			
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
		if(this.m_output!=null)  	jsonMap.put(OUTPUT, m_output.toStringValue(HAPSerializationFormat.JSON));
	}
	
	public HAPDefinitionResultActivity cloneNormalActivityResultDefinition() {
		HAPDefinitionResultActivity out = new HAPDefinitionResultActivity();
		this.cloneToEntityInfo(out);
		if(this.m_output!=null)   out.m_output = this.m_output.cloneDataAssocation();
		return out;
	}
}
