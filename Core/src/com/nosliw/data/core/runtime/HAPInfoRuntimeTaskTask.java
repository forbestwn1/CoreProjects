package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPExecutableTaskSuite;

@HAPEntityWithAttribute
public class HAPInfoRuntimeTaskTask{

	@HAPAttribute
	public static String TASKSUITE = "taskSuite";

	@HAPAttribute
	public static String ITEMNAME = "itemName";

	@HAPAttribute
	public static String INPUTVALUE = "inputValue";

	private HAPExecutableTaskSuite m_taskSuite;
	
	private String m_itemName;
	
	private Map<String, Object> m_inputValue;

	public HAPInfoRuntimeTaskTask(HAPExecutableTaskSuite activitySuite, String itemName, Map<String, Object> inputValue){
		this.m_taskSuite = activitySuite;
		this.m_itemName = itemName;
		if(HAPBasicUtility.isStringEmpty(this.m_itemName))   this.m_itemName = HAPConstantShared.NAME_DEFAULT;
		this.m_inputValue = inputValue; 
	}
	
	public HAPExecutableTaskSuite getTaskSuite(){return this.m_taskSuite;}
	
	public String getItemName() {    return this.m_itemName;   }
	
	public Map<String, Object> getInputValue(){  return this.m_inputValue;  }

}
