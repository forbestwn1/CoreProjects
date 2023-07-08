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

@HAPEntityWithAttribute(baseName="EXPRESSION")
public class HAPExecutableEntityExpression extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	//temp info
	private Map<String, HAPData> m_dataConstants;
	
	public HAPExecutableEntityExpression() {
		this.setNormalAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableCriteriaInfo());
	}

	public HAPExecutableExpression getExpression(){   return (HAPExecutableExpression)this.getNormalAttributeValue(EXPRESSION);  }
	public void setExpression(HAPExecutableExpression expression) {    this.setNormalAttributeValueObject(EXPRESSION, expression);       }
	
	public void setDataConstants(Map<String, HAPData> dataConstants) {   this.m_dataConstants = dataConstants;    }
	public Map<String, HAPData> getDataConstants(){   return this.m_dataConstants;     }
	
	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.setNormalAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return (HAPContainerVariableCriteriaInfo)this.getNormalAttributeValue(VARIABLEINFOS);    }
	
	public void discover(HAPDataTypeCriteria expectOutput, HAPDataTypeHelper dataTypeHelper, HAPProcessTracker processTracker) {
		Map<String, HAPInfoCriteria> discoveredVarsInf = new LinkedHashMap<String, HAPInfoCriteria>();
		List<HAPOperand> operands = new ArrayList<HAPOperand>();
		operands.add(this.getExpression().getOperand().getOperand());
		List<HAPDataTypeCriteria> expectedCriterias = new ArrayList<HAPDataTypeCriteria>();
		expectedCriterias.add(expectOutput);
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		
		this.setVariablesInfo(HAPUtilityOperand.discover(
				operands,
				expectedCriterias,
				this.getVariablesInfo(),
				matchers,
				dataTypeHelper,
				processTracker));

		this.getExpression().setOutputMatchers(matchers.get(0));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		dependency.addAll(this.getExpression().getResourceDependency(runtimeInfo, resourceManager));
	}
}
