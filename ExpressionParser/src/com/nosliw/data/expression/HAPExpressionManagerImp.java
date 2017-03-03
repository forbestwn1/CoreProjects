package com.nosliw.data.expression;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPExpressionManager;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	private static HAPExpressionManagerImp m_instance;
	
	public static HAPExpressionManagerImp getInstance(){
		if(m_instance==null){
			m_instance = new HAPExpressionManagerImp();
		}
		return m_instance;
	}
	
	
	public HAPExpressionManagerImp(){
		HAPValueInfoManager.getInstance().importFromXML(HAPExpressionManagerImp.class, new String[]{
				"expression.xml",
				"referenceinfo.xml"
		});
		
		
		
	}


	@Override
	public HAPExpressionInfo getExpressionInfo(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HAPExpression processExpressionInfo(HAPExpressionInfo expressionInfo) {
		//replace all reference
		
		
		//get variables information
		
		return null;
	}
	
}
