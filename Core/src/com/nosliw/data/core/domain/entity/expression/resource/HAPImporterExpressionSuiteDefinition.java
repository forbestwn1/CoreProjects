package com.nosliw.data.core.domain.entity.expression.resource;

import java.io.InputStream;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserExpression;

public class HAPImporterExpressionSuiteDefinition {

	public static HAPResourceEntityExpressionSuite readProcessSuiteDefinitionFromFile(InputStream inputStream, HAPParserExpression expressionParser){
		HAPResourceEntityExpressionSuite suite = null;
		try{
			String content = HAPUtilityFile.readFile(inputStream);
			JSONObject contentJson = HAPUtilityJson.newJsonObject(content);
//			suite = HAPParserResourceExpressionDefinition.parseExpressionSuite(contentJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return suite;
	}

}
