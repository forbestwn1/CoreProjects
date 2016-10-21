package com.nosliw.data;

import java.util.List;
import java.util.Set;

public interface HAPDataTypeManager {

	public HAPDataType getDataType(HAPDataTypeInfo dataTypeInfo);
	
	public HAPDataTypeFamily getDataTypeFamily(HAPDataTypeInfo dataTypeInfo);
	
	public List<HAPDataType> queryDataType(HAPQueryInfo queryInfo);
	
	
	public Set<String> getLanguages();
	
	public HAPExpression compileExpression(HAPExpressionInfo expressionInfo, String lang);
	
	
}
