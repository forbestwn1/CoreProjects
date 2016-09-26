package com.nosliw.application;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.datasource.realtor.HAPDataSourceRealtor;
import com.nosliw.datasource.school.HAPDataSourceSchool;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

public class Main {

	public static void main(String[] argus){
		HAPDataSourceRealtor realtorDataSource = new HAPDataSourceRealtor();
		
		HAPDataSourceSchool schoolDataSource = new HAPDataSourceSchool();
		
		HAPData realtorData = realtorDataSource.getData();
		HAPData schoolData = schoolDataSource.getData();

		String expressionDef = "";
		
		HAPDataTypeManager dataTypeMan = new HAPDataTypeManager(null);
		
		HAPExpressionInfo expressionInfo = new HAPExpressionInfo(expressionDef, null, null);
		HAPExpression expression = new HAPExpression(expressionInfo, dataTypeMan);
		
		HAPData outData = expression.execute(null, null);
		
	}
	
}
