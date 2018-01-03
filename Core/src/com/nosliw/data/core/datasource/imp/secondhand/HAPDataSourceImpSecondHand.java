package com.nosliw.data.core.datasource.imp.secondhand;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.datasource.HAPDataSource;
import com.nosliw.data.core.datasource.HAPDataSourceDefinition;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPOperandTask;
import com.nosliw.data.core.expression.HAPOperandUtility;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.core.expression.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteExpressionRhino;

public class HAPDataSourceImpSecondHand implements HAPDataSource{

	private HAPExpressionManager m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	private HAPDataSourceDefinition m_dataSourceDefinition;
	
	private HAPConfigure m_configure;
	
	public HAPDataSourceImpSecondHand(
			HAPDataSourceDefinition dataSourceDefinition, 
			HAPExpressionManager expressionManager,
			HAPRuntime runtime){
		this.m_runtime = runtime;
		this.m_expressionManager = expressionManager;
		this.m_dataSourceDefinition = dataSourceDefinition;
		
		this.m_configure = new HAPConfigure();
		this.m_configure.buildObjectByJson(this.m_dataSourceDefinition.getConfigure());
		
		//process data source operand
		this.processDataSourceOperand();
	}
	
	private void processDataSourceOperand(){
		//find mapped data source names
		Map<String, String> mappedDataSourceNames = new LinkedHashMap<String, String>();
		Map<String, HAPDependentDataSource> dependentDataSources = this.m_configure.getDependentDataSources();
		for(String mappedName : dependentDataSources.keySet()){
			mappedDataSourceNames.put(mappedName, dependentDataSources.get(mappedName).getName());
		}
		
		HAPExpressionDefinitionSuite expressionSuite = this.m_configure.getExpressionSuite();
		Map<String, HAPExpressionDefinition> expressions = expressionSuite.getAllExpressionDefinitions();
		for(String expName : expressions.keySet()){
			HAPExpressionDefinition expDef = expressions.get(expName);
			HAPOperandUtility.processAllOperand(expDef.getOperand(), mappedDataSourceNames, new HAPOperandTask(){
				@Override
				public boolean processOperand(HAPOperandWrapper operand, Object data) {
					Map<String, String> mappedDataSourceNames = (Map<String, String>)data;
					switch(operand.getOperand().getType()){
					case HAPConstant.EXPRESSION_OPERAND_VARIABLE:
						HAPOperandVariable variableOperand = (HAPOperandVariable)operand.getOperand();
						String dataSourceName = mappedDataSourceNames.get(variableOperand.getVariableName());
						if(dataSourceName!=null){
							//variable operand for data source operand
							Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>();
							Map<String, String> varConfigure = new LinkedHashMap<String, String>();
							
							Map<String, HAPDependentDataSourceParm> dependentParms = m_configure.getDependentDataSources().get(variableOperand.getVariableName()).getParms();
							for(String dataSourceParmName : dependentParms.keySet()){
								HAPDependentDataSourceParm dependentParm = dependentParms.get(dataSourceParmName);
								String mappedVariableName = dependentParm.getMappedVariableName();
								if(mappedVariableName!=null){
									//use variable
									varConfigure.put(dataSourceParmName, mappedVariableName);
								}
								else{
									//use constant
									constants.put(dataSourceParmName, dependentParm.getParmData());
								}
							}
							operand.setOperand(new  HAPOperandDataSource(dataSourceName, constants, varConfigure));							
						}
						break;
					}

					return true;
				}
			});
		}
	}
	
	
	@Override
	public HAPData getData(Map<String, HAPData> parms) {
		HAPExpression expression = m_expressionManager.processExpression(null, "main", m_configure.getExpressionSuite(), null);
		//execute expression
		HAPRuntimeTask task1 = new HAPRuntimeTaskExecuteExpressionRhino(expression, parms);
		HAPServiceData serviceData = m_runtime.executeTaskSync(task1);
		return (HAPData)serviceData.getData();
	}

	@Override
	public HAPDataSourceDefinition getDefinition() {
		return this.m_dataSourceDefinition;
	}

}
