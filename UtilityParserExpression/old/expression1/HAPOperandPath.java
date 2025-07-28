package com.nosliw.data.expression1;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core1.HAPDataTypeInfo;
import com.nosliw.data.core1.HAPDataTypeManager;
import com.nosliw.data.core1.HAPOperand;
import com.nosliw.data.core1.HAPOperationContext;
import com.nosliw.data.core1.HAPWraper;
import com.nosliw.data.expression.utils.HAPExpressionUtility;
import com.nosliw.data.expression.utils.HAPIterateOperandTask;
import com.nosliw.data.expression.utils.HAPIterateOperandTaskOut;
import com.nosliw.data.imp.expression.parser.utils.HAPAttributeConstant;

/*
 * operand that combine attribute operand together
 */
public class HAPOperandPath extends HAPOperandImp{

	//original attribute operand
	private HAPOperandAttribute m_attributeOperand;  
	
	//data to run the operation
	private HAPOperand m_baseData;
	//path
	private String m_path;

	public HAPOperandPath(HAPOperandAttribute attributeOperand, HAPDataTypeManager dataTypeMan){
		super(dataTypeMan);
		
		this.m_attributeOperand = attributeOperand;
		if(this.m_attributeOperand.getOutDataTypeInfo()!=null)   this.setOutDataTypeInfo(this.m_attributeOperand.getOutDataTypeInfo());
		
		//iterate attribute operand to build path operand
		HAPExpressionUtility.iterateOperand(m_attributeOperand, new HAPIterateOperandTask(){
			@Override
			public HAPIterateOperandTaskOut process(HAPOperand operand, Object data, boolean isRoot) {
				HAPIterateOperandTaskOut taskOut = new HAPIterateOperandTaskOut();
				
				String path = (String)data;
				
				switch(operand.getOperandType()){
				case HAPConstant.EXPRESSION_OPERAND_CONSTANT:
				case HAPConstant.EXPRESSION_OPERAND_VARIABLE:
				case HAPConstant.EXPRESSION_OPERAND_DATAOPERATION:
				case HAPConstant.EXPRESSION_OPERAND_DATATYPEOPERATION:
				{
					if(HAPBasicUtility.isStringNotEmpty(path)){
						//find the base operand for this path operand
						m_baseData = operand;
						m_path = path;
					}
					break;
				}
				case HAPConstant.EXPRESSION_OPERAND_PATHOPERATION:
				{
					if(HAPBasicUtility.isStringNotEmpty(path)){
						//find the base operand for this path operand
						HAPOperandPath pathOperand = (HAPOperandPath)operand;
						m_baseData = pathOperand.m_baseData;
						m_path = HAPNamingConversionUtility.cascadePath(pathOperand.m_path, path);
					}
					break;
				}
				case HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
				{
					HAPOperandAttribute attributeOperand = (HAPOperandAttribute)operand;
					String fullPath = HAPNamingConversionUtility.cascadePath(attributeOperand.getAttribute(), path);
					if(HAPBasicUtility.isStringEmpty(path)){
						//for first attribute in path
						if(!isRoot){
							taskOut.outOperand = new HAPOperandPath(attributeOperand, getDataTypeManager());
							taskOut.toChild = false;
						}
					}
					taskOut.childData = fullPath;
					break;
				}
				}
				return taskOut;
			}
		}, null);
	}

	@Override
	public int getOperandType() {	return HAPConstant.EXPRESSION_OPERAND_PATHOPERATION;	}

	@Override
	public HAPData execute(Map<String, HAPData> vars, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) {
		return m_attributeOperand.execute(vars, wraperVars, opContext);
	}

	@Override
	public boolean isScriptRunnable(String script) {
		return m_attributeOperand.isScriptRunnable(script);
	}

	@Override
	public void preProcess(Map<String, HAPDataTypeInfo> varsInfo, Set<HAPDataTypeInfo> dataTypeInfos) {
	}

	public HAPOperand getBaseData(){	return this.m_baseData;	}
	public void setBaseData(HAPOperand operand){  this.m_baseData = operand; }
	public String getPath(){ return this.m_path; }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonDataTypeMap) {
		jsonMap.put(HAPAttributeConstant.OPERAND_PATH_BASEDATA, this.m_baseData.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(HAPAttributeConstant.OPERAND_PATH_PATH, this.m_path);
	}
}
