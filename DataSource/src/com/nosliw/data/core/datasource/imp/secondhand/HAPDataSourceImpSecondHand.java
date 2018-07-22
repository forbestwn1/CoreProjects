package com.nosliw.data.core.datasource.imp.secondhand;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.datasource.HAPExecutableDataSource;
import com.nosliw.data.core.datasource.HAPDefinition;
import com.nosliw.data.core.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.data.core.datasource.HAPDefinitionParm;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPDataSourceImpSecondHand implements HAPExecutableDataSource{

	@Override
	public HAPData getData(Map<String, HAPData> parms) {
		// TODO Auto-generated method stub
		return null;
	}
/*
implements HAPDataSource{

	private HAPDataSourceDefinitionManager m_dataSourceDefinitionManager;
	
	private HAPManagerTask m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	private HAPDefinition m_dataSourceDefinition;
	
	private HAPConfigure m_configure;
	
	public HAPDataSourceImpSecondHand(
			HAPDefinition dataSourceDefinition, 
			HAPDataSourceDefinitionManager dataSourceDefinitionManager,
			HAPManagerTask expressionManager,
			HAPRuntime runtime){
		this.m_dataSourceDefinitionManager = dataSourceDefinitionManager;
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
		
		HAPDefinitionTaskSuite expressionSuite = this.m_configure.getExpressionSuite();
		Map<String, HAPDefinitionTask> expressions = expressionSuite.getAllTaskDefinitions();
		for(String expName : expressions.keySet()){
			HAPDefinitionTask expDef = expressions.get(expName);
			
			//add data source parms to expression as variable
			Map<String, HAPDefinitionParm> dataSourceParms = this.m_dataSourceDefinition.getParms();
			for(String parmName : dataSourceParms.keySet()){
				expDef.getVariableCriterias().put(parmName, dataSourceParms.get(parmName).getVaraibleInfo());
			}
			
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
							operand.setOperand(new HAPOperandDataSource(dataSourceName, m_dataSourceDefinitionManager.getDataSourceDefinition(dataSourceName), constants, varConfigure));							
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
		HAPRuntimeTask task = new HAPRuntimeTaskExecuteExpressionRhino(expression, parms);
		HAPServiceData serviceData = m_runtime.executeTaskSync(task);
		return HAPDataUtility.buildDataWrapperFromJson((JSONObject)serviceData.getData());
	}

	public HAPDefinition getDefinition() {
		return this.m_dataSourceDefinition;
	}
*/
}
