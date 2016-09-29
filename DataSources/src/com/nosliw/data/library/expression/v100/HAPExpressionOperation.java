package com.nosliw.data.library.expression.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.basic.map.HAPMapData;
import com.nosliw.data.library.entity.v100.HAPEntity;
import com.nosliw.expression.HAPExpression;

public class HAPExpressionOperation extends HAPDataOperation{

	public HAPExpressionOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "expression:simple", "map:simple" }, out = "any", description = "Excute Expression")
	public HAPData excute(HAPData[] parms){
		HAPExpressionData expressionData = (HAPExpressionData)parms[0];
		HAPExpression expression = expressionData.getExpression();

		HAPMapData varsData = (HAPMapData)parms[1];
		
		HAPData out = expression.execute(varsData.getMap(), null);
		return out;
	}


}
