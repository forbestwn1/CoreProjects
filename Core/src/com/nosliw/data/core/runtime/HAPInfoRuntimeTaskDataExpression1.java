package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableEntityExpressionDataGroup;

@HAPEntityWithAttribute
public class HAPInfoRuntimeTaskDataExpression1{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String ITEMNAME = "itemName";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";

	
	private HAPExecutableEntityExpressionDataGroup m_expression;
	
	private String m_itemName;
	
	private Map<String, HAPData> m_variablesValue;

	private Map<String, HAPData> m_referencesValue;
	
	public HAPInfoRuntimeTaskDataExpression1(HAPExecutableEntityExpressionDataGroup expression, String itemName, Map<String, HAPData> variablesValue, Map<String, HAPData> referencesValue){
		this.m_expression = expression;
		this.m_itemName = itemName;
		if(HAPUtilityBasic.isStringEmpty(this.m_itemName))   this.m_itemName = HAPConstantShared.NAME_DEFAULT;
		this.m_variablesValue = variablesValue; 
		this.m_referencesValue = referencesValue;
	}
	
	public HAPExecutableEntityExpressionDataGroup getExpression(){return this.m_expression;}
	
	public String getItemName() {    return this.m_itemName;   }
	
	public Map<String, HAPData> getVariablesValue(){  return this.m_variablesValue;  }

	public Map<String, HAPData> getReferencesValue(){  return this.m_referencesValue;  }
	


}
