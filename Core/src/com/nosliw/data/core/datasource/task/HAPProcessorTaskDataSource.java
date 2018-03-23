package com.nosliw.data.core.datasource.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.data.core.datasource.HAPDefinition;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPProcessorTask;
import com.nosliw.data.core.task.HAPUpdateVariableDomain;
import com.nosliw.data.core.task.HAPUpdateVariableMap;

public class HAPProcessorTaskDataSource implements HAPProcessorTask{

	private HAPDataSourceDefinitionManager m_dataSourceDefMan;
	
	public HAPProcessorTaskDataSource(HAPDataSourceDefinitionManager dataSourceDefMan) {
		this.m_dataSourceDefMan = dataSourceDefMan;
	}
	
	@Override
	public HAPExecutableTask process(HAPDefinitionTask taskDefinition, String domain, Map<String, String> variableMap,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {

		HAPDefinitionTaskDataSource dataSourceTaskDef = (HAPDefinitionTaskDataSource)taskDefinition;
		
		HAPExecutableTaskDataSource out = new HAPExecutableTaskDataSource(this.m_dataSourceDefMan.getDataSourceDefinition(dataSourceTaskDef.getDataSource()));
	
		//parms
		Map<String, HAPDefinitionExpression> parmsDef = dataSourceTaskDef.getParmsDef();
		HAPDefinition dataSourceDefintion = out.getDataSourceDefinition();
		for(String dsParm : dataSourceDefintion.getParms().keySet()) {
			if(parmsDef.get(dsParm)!=null) {
				out.addParmOperand(dsParm, parmsDef.get(dsParm).getOperand().getOperand().cloneOperand());
			}
			else {
				//if not defined, then use datasource variable name
				out.addParmOperand(dsParm, new HAPOperandVariable(dsParm));
			}
		}
		
		out.updateVariable(new HAPUpdateVariableDomain(domain));
		
		//get updated variables map according to domain
		Map<String, String> domainedVariableMap = new LinkedHashMap<String, String>();
		if(variableMap!=null) {
			for(String name : variableMap.keySet()) {
				domainedVariableMap.put(HAPExpressionUtility.buildFullVariableName(domain, name), variableMap.get(name));
			}
		}
		//update variable in task
		out.updateVariable(new HAPUpdateVariableMap(domainedVariableMap));

		return out;
	}

}
