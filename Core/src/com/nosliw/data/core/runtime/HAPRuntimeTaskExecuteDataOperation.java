package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.expression.HAPExpression;

@HAPEntityWithAttribute
public abstract class HAPRuntimeTaskExecuteDataOperation extends HAPRuntimeTask{

	final public static String TASK = "ExecuteDataOperation"; 
	
	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";
	@HAPAttribute
	public static String OPERATION = "operation";
	@HAPAttribute
	public static String PARMS = "parms";

	private HAPDataTypeId m_dataTypeId;
	
	private String m_operation;
	
	private Map<String, HAPData> m_parms;
	
	public HAPRuntimeTaskExecuteDataOperation(HAPDataTypeId dataTypeId, String operation, Map<String, HAPData> parms){
		this.m_dataTypeId = dataTypeId;
		this.m_operation = operation;
		this.m_parms = parms;
	}
	
	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;  }
	
	public String getOperation(){   return this.m_operation;  }
	
	public Map<String, HAPData> getParms(){  return this.m_parms;  }
	
	public HAPData getDataOperationResult(){ return (HAPData)this.getResult(); }

	@Override
	public String getTaskType(){  return TASK; }

}
