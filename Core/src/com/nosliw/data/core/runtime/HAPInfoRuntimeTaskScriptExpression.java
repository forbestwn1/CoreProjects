package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPInfoRuntimeTaskScriptExpression{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String ITEMNAME = "itemName";

	private String m_expressionGroupResourceId;
	
	private String m_itemName;
	
	public HAPInfoRuntimeTaskScriptExpression(String expressionGroupResourceId, String itemName){
		this.m_expressionGroupResourceId = expressionGroupResourceId;
		this.m_itemName = itemName;
		if(HAPUtilityBasic.isStringEmpty(this.m_itemName))   this.m_itemName = HAPConstantShared.NAME_DEFAULT;
	}
	
	public String getExpressionGroupResourceId() {     return this.m_expressionGroupResourceId;     }
	
	public String getItemName() {    return this.m_itemName;   }

}
