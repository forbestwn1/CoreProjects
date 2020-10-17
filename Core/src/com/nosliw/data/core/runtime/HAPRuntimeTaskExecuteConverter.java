package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.matcher.HAPMatchers;

@HAPEntityWithAttribute
public abstract class HAPRuntimeTaskExecuteConverter extends HAPRuntimeTask{

	final public static String TASK = "ExecuteConverter"; 
	
	@HAPAttribute
	public static String DATAT = "data";
	@HAPAttribute
	public static String MATCHERS = "matchers";

	private HAPData m_data;
	
	private HAPMatchers m_matchers;
	
	public HAPRuntimeTaskExecuteConverter(HAPData data, HAPMatchers matchers){
		this.m_data = data;
		this.m_matchers = matchers;
	}
	
	public HAPData getData() {  return this.m_data;  }
	public HAPMatchers getMatchers() {  return this.m_matchers;   }
	
	public HAPData getConverterResult(){ return (HAPData)this.getResult(); }

	@Override
	public String getTaskType(){  return TASK; }

}
