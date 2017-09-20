package com.nosliw.uiresource;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

/*
 * class for script expression part in html content
 */
@HAPEntityWithAttribute
public class HAPScriptExpressionInContent extends HAPSerializableImp{

	private HAPScriptExpression m_scriptExpression;
	
	public HAPScriptExpressionInContent(HAPScriptExpression scriptExpression){
		this.m_scriptExpression = scriptExpression;
	}
	
	
}
