package com.nosliw.data.library.expression.v100;

import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

public class HAPExpressionData extends HAPDataImp{

	private HAPExpression m_expression;
	
	public HAPExpressionData(HAPDataType dataType) {
		super(dataType);
	}

	public HAPExpression getExpression(){
		return this.m_expression;
	}
	
	public HAPExpression newExpression(String expression, Map<String, HAPData> constantDatas, Map<String, HAPDataTypeInfo> varDataTypeInfos){
		HAPExpressionInfo info = new HAPExpressionInfo(expression, constantDatas, varDataTypeInfos);
		HAPExpression out = new HAPExpression(info, this.getDataTypeManager());
		return out;
	}
	
	@Override
	public HAPData cloneData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		// TODO Auto-generated method stub
		return null;
	}

}
