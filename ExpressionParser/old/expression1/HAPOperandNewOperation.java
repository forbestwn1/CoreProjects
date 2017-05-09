package com.nosliw.data.expression1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core1.HAPDataTypeInfo;
import com.nosliw.data.core1.HAPDataTypeManager;
import com.nosliw.data.core1.HAPOperand;
import com.nosliw.data.core1.HAPOperationContext;
import com.nosliw.data.core1.HAPWraper;

public class HAPOperandNewOperation extends HAPOperandOperation{
	
	public HAPOperandNewOperation(HAPDataTypeInfo dataTypeInfo, HAPOperand[] parms, HAPDataTypeManager dataTypeMan){
		super(null, parms, dataTypeMan);
		this.m_baseDataTypeInfo = dataTypeInfo;
		this.m_baseDataType = this.getDataTypeManager().getDataType(m_baseDataTypeInfo);
		this.setOutDataTypeInfo(this.m_baseDataTypeInfo);
	}

	@Override
	public int getOperandType() {return HAPConstant.EXPRESSION_OPERAND_NEWOPERATION;}

	@Override
	public HAPData execute(Map<String, HAPData> vars, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) {
		HAPData[] parms = new HAPData[this.getParameters().length];
		for(int i=0; i<this.getParameters().length; i++){
			parms[i] = this.getParameters()[i].execute(vars, wraperVars, opContext);
		}
		HAPServiceData serviceData = null;
		if(m_operationInfo==null)	serviceData = this.getBaseDataType().newData(parms, opContext);
		else	serviceData = this.getBaseDataType().newData(this.m_operationInfo.getName(), parms, opContext);
		return (HAPData)serviceData.getData();
	}
	
	@Override
	public void preProcess(Map<String, HAPDataTypeInfo> varsInfo, Set<HAPDataTypeInfo> dataTypeInfos){
		//parms data type info
		for(HAPOperand parm : this.getParameters()){
			parm.preProcess(varsInfo, dataTypeInfos);
		}
		
		List<HAPDataTypeInfo> parmDataTypeInfos = new ArrayList<HAPDataTypeInfo>();
		boolean parmDefined = true;
		for(HAPOperand parm : this.getParameters()){
			HAPDataTypeInfo parmDataTypeInfo = parm.getOutDataTypeInfo();
			if(parmDataTypeInfo!=null){
				parmDataTypeInfos.add(parmDataTypeInfo);
			}
			else{
				parmDefined = false;
				break;
			}
		}
		
		if(parmDefined==true){
			//get new operation
			this.m_operationInfo = this.m_baseDataType.getNewDataOperation(parmDataTypeInfos.toArray(new HAPDataTypeInfo[0]));
		}
		
		super.preProcess(varsInfo, dataTypeInfos);
	}
	
	@Override
	public boolean isScriptRunnable(String script)
	{
		//check parms
		for(HAPOperand parm : this.getParameters()){
			if(!parm.isScriptRunnable(script))  return false;
		}

		//check operation script
		if(!this.isOperationScriptAvailableLocally(script))   return false;
		
		return true;
	}

}
