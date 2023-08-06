package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableExpressionData;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;

public class HAPExecutableEntityExpressionScriptGroup extends HAPExecutableEntityComplex{
	
	@HAPAttribute
	public static String EXPRESSIONS = "expressions";
	
	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";
	
	public HAPExecutableEntityExpressionScriptGroup() {
		this.setAttributeValueObject(EXPRESSIONS, new ArrayList<HAPExecutableExpressionData>());
		this.setAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableCriteriaInfo());
	}

	public List<HAPExecutableExpression> getAllExpressionItems(){   return (List<HAPExecutableExpression>)this.getAttributeValue(EXPRESSIONS);  }
	public void addExpressionItem(HAPExecutableExpression expressionItem) {    this.getAllExpressionItems().add(expressionItem);       }

	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.setAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return (HAPContainerVariableCriteriaInfo)this.getAttributeValue(VARIABLEINFOS);    }
}
