package com.nosliw.data.core.datasource;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExpressionManager;


@HAPEntityWithAttribute
public class HAPDefinitionParm extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String CRITERIA = "criteria";
	
	@HAPAttribute
	public static String DEFAULT = "default";

	private String m_name;
	
	private String m_description;
	
	private HAPVariableInfo m_variableInfo;
	
	private HAPData m_default;

	public String getName(){   return this.m_name;   }

	public String getDescription(){   return this.m_description;   }
	
	public HAPVariableInfo getVaraibleInfo(){   return this.m_variableInfo;   }
	
	public HAPData getDefault(){   return this.m_default;   }
	
	public HAPDefinitionParm(){
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_name = objJson.optString(NAME);
			this.m_description = objJson.optString(DESCRIPTION);
			this.m_variableInfo = HAPVariableInfo.buildVariableInfo(HAPExpressionManager.criteriaParser.parseCriteria(objJson.getString(CRITERIA)));
			
			JSONObject defaultJson = objJson.optJSONObject(DEFAULT);
			if(defaultJson!=null)	this.m_default = HAPDataUtility.buildDataWrapperFromJson(defaultJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
}
