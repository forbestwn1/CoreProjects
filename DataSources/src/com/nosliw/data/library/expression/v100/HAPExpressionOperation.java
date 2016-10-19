package com.nosliw.data.library.expression.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.map.HAPMapData;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.library.entity.v100.HAPEntity;
import com.nosliw.data1.HAPDataOperation;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPOperationContext;
import com.nosliw.data1.HAPOperationInfoAnnotation;
import com.nosliw.expression.HAPExpression;

public class HAPExpressionOperation extends HAPDataOperation{

	public HAPExpressionOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "expression:simple", "map:simple" }, out = "any", description = "Excute Expression")
	public HAPData execute(HAPData[] parms, HAPOperationContext opContext){
		HAPExpressionData expressionData = (HAPExpressionData)parms[0];
		HAPExpression expression = expressionData.getExpression();

		HAPMapData varsData = (HAPMapData)parms[1];
		
		HAPData out = expression.execute(varsData.getMap(), null, opContext);
		return out;
	}

	@HAPOperationInfoAnnotation(in = { "string:simple", "map:simple" }, out = "any", description = "Excute Expression")
	public HAPData newData(HAPData[] parms, HAPOperationContext opContext){
		HAPStringData expressionData = (HAPStringData)parms[0];
		HAPMapData constantsData = (HAPMapData)parms[1];
		
		HAPExpressionType expressionType = (HAPExpressionType)this.getDataType();
		return expressionType.newExpression(expressionData.getValue(), constantsData.getMap(), null);
	}

}
