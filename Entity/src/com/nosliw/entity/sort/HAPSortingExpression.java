package com.nosliw.entity.sort;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data1.HAPDataTypeInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;
import com.nosliw.expression.utils.HAPExpressionUtility;

/*
 * 
 */
public class HAPSortingExpression extends HAPSortingInfo{

	private HAPExpression m_expression;

	public HAPSortingExpression(HAPExpressionInfo expression, HAPDataTypeInfo dataTypeInfo, HAPDataTypeManager dataTypeMan) {
		super(HAPConstant.SORTING_TYPE_EXPRESSION, dataTypeMan);
		
		Map<String, HAPDataTypeInfo> expressionDataTypeInfos = new LinkedHashMap<String, HAPDataTypeInfo>();
		expressionDataTypeInfos.put(HAPConstant.EXPRESSION_VARIABLE_DATA1, dataTypeInfo);
		expressionDataTypeInfos.put(HAPConstant.EXPRESSION_VARIABLE_DATA2, dataTypeInfo);
		this.m_expression = new HAPExpression(expression.addVariableInfo(expressionDataTypeInfos), this.getDataTypeManager());
	}

	@Override
	public int compare(HAPWraper data1, HAPWraper data2) {
		Map<String, HAPWraper> datas = new LinkedHashMap<String, HAPWraper>();
		datas.put(HAPConstant.EXPRESSION_VARIABLE_DATA1, data1);
		datas.put(HAPConstant.EXPRESSION_VARIABLE_DATA2, data2);
		
		int out = HAPConstant.COMPARE_EQUAL;
		try {
			out = HAPExpressionUtility.executeIntegerExpression(m_expression, null, datas);
		} catch (HAPServiceDataException e) {
			e.printStackTrace();
		}
		return out; 
	}
}
