package com.nosliw.data.core.service;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionManager;

@HAPEntityWithAttribute
public class HAPDefinitionServiceParm extends HAPEntityInfoImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";
	
	@HAPAttribute
	public static String DEFAULT = "default";

	private HAPDataTypeCriteria m_criteria;
	
	private HAPData m_default;
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_criteria;   }
	
	public HAPData getDefault(){   return this.m_default;   }
	
	public HAPDefinitionServiceParm(){	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);

			this.m_criteria = HAPExpressionManager.criteriaParser.parseCriteria(objJson.getString(CRITERIA));
			
			JSONObject defaultJson = objJson.optJSONObject(DEFAULT);
			if(defaultJson!=null)	this.m_default = HAPDataUtility.buildDataWrapperFromJson(defaultJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
}
