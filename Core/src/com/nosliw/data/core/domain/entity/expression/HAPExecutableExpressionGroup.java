package com.nosliw.data.core.domain.entity.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute(baseName="EXPRESSIONGROUP")
public class HAPExecutableExpressionGroup extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static String EXPRESSIONS = "expressions";

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	//temp info
	private Map<String, HAPData> m_dataConstants;
	
	public HAPExecutableExpressionGroup() {
		this.setNormalAttributeValueObject(EXPRESSIONS, new ArrayList<HAPExecutableExpression>());
		this.setNormalAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableCriteriaInfo());
	}

	public List<HAPExecutableExpression> getExpressionItems(){   return (List<HAPExecutableExpression>)this.getNormalAttributeValue(EXPRESSIONS);  }
	public void addExpressionItem(HAPExecutableExpression expressionItem) {    this.getExpressionItems().add(expressionItem);       }
	

	public void setDataConstants(Map<String, HAPData> dataConstants) {   this.m_dataConstants = dataConstants;    }
	public Map<String, HAPData> getDataConstants(){   return this.m_dataConstants;     }
	
	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.setNormalAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return (HAPContainerVariableCriteriaInfo)this.getNormalAttributeValue(VARIABLEINFOS);    }
	
	public void discover(Map<String, HAPDataTypeCriteria> expectOutput, HAPDataTypeHelper dataTypeHelper, HAPProcessTracker processTracker) {
		Map<String, HAPInfoCriteria> discoveredVarsInf = new LinkedHashMap<String, HAPInfoCriteria>();
		List<String> names = new ArrayList<String>();
		List<HAPOperand> operands = new ArrayList<HAPOperand>();
		List<HAPDataTypeCriteria> outPutCriteria = new ArrayList<HAPDataTypeCriteria>();
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		
		for(HAPExecutableExpression expression : this.getExpressionItems()) {
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
				discoveredVarsInf,
				matchers,
				dataTypeHelper,
				processTracker));
		
		for(int i=0; i<names.size(); i++) {
			this.getExpressionItems().get(i).setOutputMatchers(matchers.get(i));
		}
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		for(HAPExecutableExpression expression : this.getExpressionItems()) {
			dependency.addAll(expression.getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
