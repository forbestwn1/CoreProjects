package com.nosliw.data.core.domain.entity.expression.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.dataexpression.HAPExecutableExpressionData;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionDataGroup extends HAPExecutableEntityExpressionData{

	@HAPAttribute
	public static String EXPRESSIONS = "expressions";

	//temp info
	private Map<String, HAPData> m_dataConstants;
	
	public HAPExecutableEntityExpressionDataGroup() {
		this.setAttributeValueObject(EXPRESSIONS, new ArrayList<HAPExecutableExpressionData>());
	}

	@Override
	public List<HAPExecutableExpressionData> getAllExpressionItems(){   return (List<HAPExecutableExpressionData>)this.getAttributeValue(EXPRESSIONS);  }
	public void addExpressionItem(HAPExecutableExpressionData expressionItem) {    this.getAllExpressionItems().add(expressionItem);       }
	

	public void setDataConstants(Map<String, HAPData> dataConstants) {   this.m_dataConstants = dataConstants;    }
	public Map<String, HAPData> getDataConstants(){   return this.m_dataConstants;     }
	
	public void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPDataTypeHelper dataTypeHelper, HAPProcessTracker processTracker) {
		List<String> names = new ArrayList<String>();
		List<HAPOperand> operands = new ArrayList<HAPOperand>();
		List<HAPDataTypeCriteria> outPutCriteria = new ArrayList<HAPDataTypeCriteria>();
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		
		for(HAPExecutableExpressionData expression : this.getAllExpressionItems()) {
			String name = expression.getId();
			names.add(name);
			if(expectOutput==null)  outPutCriteria.add(null);
			else outPutCriteria.add(expectOutput.get(name));
			operands.add(expression.getOperand().getOperand());
		}
		
		this.setVariablesInfo(HAPUtilityOperand.discover(
				operands,
				outPutCriteria,
				this.getVariablesInfo(),
				matchers,
				dataTypeHelper,
				processTracker));
		
		for(int i=0; i<names.size(); i++) {
			this.getAllExpressionItems().get(i).setOutputMatchers(matchers.get(i));
		}
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManager resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		for(HAPExecutableExpressionData expression : this.getAllExpressionItems()) {
			dependency.addAll(expression.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
