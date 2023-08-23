package com.nosliw.data.core.runtime;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPInfoRuntimeTaskDataExpressionGroup{

	private String m_expressionGroupResourceId;
	
	private String m_itemName;
	
	public HAPInfoRuntimeTaskDataExpressionGroup(String expressionGroupResourceId, String itemName){
		this.m_expressionGroupResourceId = expressionGroupResourceId;
		this.m_itemName = itemName;
		if(HAPUtilityBasic.isStringEmpty(this.m_itemName))   this.m_itemName = HAPConstantShared.NAME_DEFAULT;
	}
	
	public String getExpressionGroupResourceId() {     return this.m_expressionGroupResourceId;     }
	
	public String getItemName() {    return this.m_itemName;   }
	
}
