package com.nosliw.uiresource;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPConstantDef extends HAPSerializableImp{

	private String m_literate;
	
	private Object m_value;
	
	private boolean m_isProcessed = false;
	
	public HAPConstantDef(String literate){
		
	}

	/**
	 * get data after processing the constant
	 * @return
	 */
	public Object getValue(){
		
	}
	
	/**
	 * 
	 */
	public void process(){
		
	}
	
	
}
