package com.nosliw.datasource.imp.secondhand;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPOperandOperation;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPOperandTask;
import com.nosliw.data.core.expression.HAPOperandUtility;
import com.nosliw.data.core.expression.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.datasource.HAPDataSource;
import com.nosliw.datasource.HAPDataSourceDefinition;
import com.nosliw.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.datasource.HAPDataSourceParm;

public class HAPDataSourceImpSecondHand implements HAPDataSource{

	private HAPDataSourceDefinitionManager m_dataSourceDefinitionManager;
	
	private HAPExpressionManager m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	private HAPDataSourceDefinition m_dataSourceDefinition;
	
	private HAPConfigure m_configure;
	
	public HAPDataSourceImpSecondHand(HAPDataSourceDefinition dataSourceDefinition){
		this.m_dataSourceDefinition = dataSourceDefinition;
	}
	
	private void init(){
		
		HAPOperandUtility.processAllOperand(expression.getOperand(), result, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Set<HAPResourceId> resourceIds = (Set<HAPResourceId>)data;
				switch(operand.getOperand().getType()){
				case HAPConstant.EXPRESSION_OPERAND_OPERATION:
					HAPOperandOperation operationOperand = (HAPOperandOperation)operand.getOperand();
					HAPOperationId operationId = operationOperand.getOperationId();
					//operation as resource
					if(operationId!=null)	resourceIds.add(HAPResourceHelper.getInstance().buildResourceIdFromIdData(operationId));
					break;
				case HAPConstant.EXPRESSION_OPERAND_REFERENCE:
					HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
					List<HAPResourceId> referenceResources = discoverResources(referenceOperand.getExpression());
					resourceIds.addAll(referenceResources);
					break;
				}

				//converter as resource
				for(HAPDataTypeConverter converter : operand.getOperand().getConverters()){
					resourceIds.add(new HAPResourceIdConverter(converter));
				}
				
				return true;
			}
		});
		
		
		
		HAPExpressionDefinitionSuite expressionSuite = this.m_configure.getExpressionSuite();
		
		Map<String, HAPDataSourceParm> dataSourceParmsDef =  this.m_dataSourceDefinition.getParms();
		
		
		//replace operation 
		
		
		
		
		Map<String, HAPDependentDataSource> dependencys = this.m_configure.getDependentDataSources();
		for(String depName : dependencys.keySet()){
			//process dependency data source
			HAPDependentDataSource dependency = dependencys.get(depName);
			String dataSourceName = dependency.getName();
			HAPDataSourceDefinition depDataSourceDef = this.m_dataSourceDefinitionManager.getDataSourceDefinition(dataSourceName);
			for(String dataSourceParmName : dataSourceParmsDef.keySet()){
				HAPDataSourceParm depDataSourceParm = dataSourceParmsDef.get(dataSourceParmName);
				//each parm in dependent data source
				String parmName = depDataSourceParm.getName();
				HAPDependentDataSourceParm parmMapping = dependency.getParmMapping(parmName);
				if(parmMapping==null){
					//no mapping for parm, merge to parent
					dataSourceParmsDef.get()
				}
				else{
					//found mapping for parm, 
				}
			}
			
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
