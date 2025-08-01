package com.nosliw.core.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.runtime.HAPRuntimeTask;

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
