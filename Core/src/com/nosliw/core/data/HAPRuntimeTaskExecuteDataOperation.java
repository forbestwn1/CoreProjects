package com.nosliw.core.data;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.runtime.HAPRuntimeTask;

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
	
	private List<HAPOperationParm> m_parms;
	
	public HAPRuntimeTaskExecuteDataOperation(HAPDataTypeId dataTypeId, String operation, List<HAPOperationParm> parms){
		this.m_dataTypeId = dataTypeId;
		this.m_operation = operation;
		this.m_parms = parms;
	}
	
	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;  }
	
	public String getOperation(){   return this.m_operation;  }
	
	public List<HAPOperationParm> getParms(){  return this.m_parms;  }
	
	public HAPData getDataOperationResult(){ return (HAPData)this.getResult(); }

	@Override
	public String getTaskType(){  return TASK; }

}
