package com.nosliw.data.core.expression.resource;

import java.io.InputStream;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPParserExpression;

public class HAPImporterExpressionSuiteDefinition {

	public static HAPResourceEntityExpressionSuite readProcessSuiteDefinitionFromFile(InputStream inputStream, HAPParserExpression expressionParser){
		HAPResourceEntityExpressionSuite suite = null;
		try{
			String content = HAPFileUtility.readFile(inputStream);
			JSONObject contentJson = HAPJsonUtility.newJsonObject(content);
//			suite = HAPParserResourceExpressionDefinition.parseExpressionSuite(contentJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}

}
