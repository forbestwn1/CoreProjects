package com.nosliw.data.library.expression.v100;

import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.expression1.HAPExpression;
import com.nosliw.data.expression1.HAPExpressionInfo;
import com.nosliw.data.imp.HAPDataImp;
import com.nosliw.data1.HAPDataTypeInfo;

public class HAPExpressionData extends HAPDataImp{

	private HAPExpression m_expression;
	
	public HAPExpressionData(HAPDataType dataType, String expression, Map<String, HAPData> constantDatas, Map<String, HAPDataTypeInfo> varDataTypeInfos){
		super(dataType);
		HAPExpressionInfo info = new HAPExpressionInfo(expression, constantDatas, varDataTypeInfos);
		this.m_expression = new HAPExpression(info, this.getDataTypeManager());
	}

	public HAPExpression getExpression(){
		return this.m_expression;
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
