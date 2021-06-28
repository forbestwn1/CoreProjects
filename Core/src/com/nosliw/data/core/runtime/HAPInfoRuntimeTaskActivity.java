package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPExecutableActivitySuite;

@HAPEntityWithAttribute
public class HAPInfoRuntimeTaskActivity{

	@HAPAttribute
	public static String ACTIVITYSUITE = "activitySuite";

	@HAPAttribute
	public static String ITEMNAME = "itemName";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";

	
	private HAPExecutableActivitySuite m_activitySuite;
	
	private String m_itemName;
	
	private Map<String, Object> m_inputValue;

	public HAPInfoRuntimeTaskActivity(HAPExecutableActivitySuite activitySuite, String itemName, Map<String, Object> inputValue){
		this.m_activitySuite = activitySuite;
		this.m_itemName = itemName;
		if(HAPBasicUtility.isStringEmpty(this.m_itemName))   this.m_itemName = HAPConstantShared.NAME_DEFAULT;
		this.m_inputValue = inputValue; 
	}
	
	public HAPExecutableActivitySuite getActivitySuiite(){return this.m_activitySuite;}
	
	public String getItemName() {    return this.m_itemName;   }
	
	public Map<String, Object> getInputValue(){  return this.m_inputValue;  }

}
