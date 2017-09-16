package com.nosliw.uiresource;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

/*
 * class that 
 */
@HAPEntityWithAttribute
public class HAPUIExpressionContent extends HAPSerializableImp{

	private HAPUIExpression m_uiExpression;
	
	public HAPUIExpressionContent(HAPUIExpression uiExpression){
		this.m_uiExpression = uiExpression;
	}
	
	public  HAPUIExpressionContent(String uiId, String content){
		this.m_uiExpression = new HAPUIExpression(uiId, content);
	}
	
}
