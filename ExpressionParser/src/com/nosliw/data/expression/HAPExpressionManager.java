package com.nosliw.data.expression;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;

public class HAPExpressionManager {

	private static HAPExpressionManager m_instance;
	
	public static HAPExpressionManager getInstance(){
		if(m_instance==null){
			m_instance = new HAPExpressionManager();
		}
		return m_instance;
	}
	
	
	public HAPExpressionManager(){
		HAPValueInfoManager.getInstance().importFromXML(HAPExpressionManager.class, new String[]{
				"expression.xml",
				"referenceinfo.xml"
		});
		
		
		
	}
	
}
