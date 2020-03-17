package com.nosliw.data.core.expression;

import java.io.InputStream;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPImporterExpressionSuiteDefinition {

	public static HAPDefinitionExpressionSuite readProcessSuiteDefinitionFromFile(InputStream inputStream, HAPParserExpression expressionParser){
		HAPDefinitionExpressionSuite suite = null;
		try{
			String content = HAPFileUtility.readFile(inputStream);
			JSONObject contentJson = HAPJsonUtility.newJsonObject(content);
			suite = HAPParserExpressionDefinition.parseExpressionSuite(contentJson, expressionParser);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}

}
