package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.domain.HAPExecutablePackage;

@HAPEntityWithAttribute
public class HAPInfoRuntimeTaskDataExpression{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String ITEMNAME = "itemName";

	private HAPExecutablePackage m_expression;
	
	private String m_itemName;
	
	public HAPInfoRuntimeTaskDataExpression(HAPExecutablePackage expression, String itemName){
		this.m_expression = expression;
		this.m_itemName = itemName;
		if(HAPUtilityBasic.isStringEmpty(this.m_itemName))   this.m_itemName = HAPConstantShared.NAME_DEFAULT;
	}
	
	public HAPExecutablePackage getExpression(){return this.m_expression;}
	
	public String getItemName() {    return this.m_itemName;   }
	
}
