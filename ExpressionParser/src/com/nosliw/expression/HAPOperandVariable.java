package com.nosliw.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.expression.utils.HAPAttributeConstant;

/*
 * Operand with variable type
 * the value of variable can be determined during run time by setting value to each variable 
 */
public class HAPOperandVariable extends HAPOperandAtom{

	//variable name
	private String m_varName;
	
	public HAPOperandVariable(String varName, HAPDataTypeManager dataTypeMan){
		super(dataTypeMan);
		this.m_varName = varName;
	}

	public HAPOperandVariable(String varName, HAPDataTypeInfo dataTypeInfo, HAPDataTypeManager dataTypeMan){
		super(dataTypeInfo, dataTypeMan);
		this.m_varName = varName;
	}
	
	@Override
	public int getOperandType() {	return HAPConstant.EXPRESSION_OPERAND_VARIABLE;	}

	public String getVarName(){	return this.m_varName;}
	
	@Override
	public HAPData execute(Map<String, HAPData> vars, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) {
		HAPData out = null;
		
		if(vars!=null){
			out = vars.get(this.getVarName());
		}
		if(out==null){
			if(wraperVars!=null){
				HAPWraper wraper = wraperVars.get(this.getVarName());
				if(wraper!=null)   out = wraper.getData();
			}
		}
		return out;
	}

	@Override
	public void preProcess(Map<String, HAPDataTypeInfo> varsInfo, Set<HAPDataTypeInfo> dataTypeInfos){
		if(!varsInfo.containsKey(this.m_varName)){
			//not on variable list, add to list
			varsInfo.put(this.m_varName, this.getOutDataTypeInfo());
		}
		else{
			//on the list, then set out data type info
			this.setOutDataTypeInfo(varsInfo.get(this.m_varName));
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonDataTypeMap){
		super.buildJsonMap(jsonMap, jsonDataTypeMap);
		jsonMap.put(HAPAttributeConstant.OPERAND_VARIABLE_VARNAME, this.m_varName);
	}
}
