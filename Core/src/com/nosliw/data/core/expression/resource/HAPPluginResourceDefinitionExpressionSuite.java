package com.nosliw.data.core.expression.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.expression.HAPImporterExpressionSuiteDefinition;
import com.nosliw.data.core.expression.HAPParserExpression;
import com.nosliw.data.core.expression.HAPResourceIdExpressionSuite;
import com.nosliw.data.core.expression.HAPUtilityExpression;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPPluginResourceDefinitionExpressionSuite implements HAPPluginResourceDefinition{

	private HAPParserExpression m_expressionParser;
	
	public HAPPluginResourceDefinitionExpressionSuite(HAPParserExpression expressionParser) {
		this.m_expressionParser = expressionParser;
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSIONSUITE;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		HAPResourceDefinitionExpressionSuite suite = null;
		try {
			HAPResourceIdExpressionSuite expressionSuiteResourceId = new HAPResourceIdExpressionSuite(resourceId);
			suite = HAPImporterExpressionSuiteDefinition.readProcessSuiteDefinitionFromFile(new FileInputStream(new File(HAPSystemFolderUtility.getExpressionFolder()+expressionSuiteResourceId.getId()+".res")), this.m_expressionParser);
			HAPUtilityExpression.normalizeReference(suite);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return suite;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return HAPParserResourceExpressionDefinition.parseExpressionSuite(jsonObj, this.m_expressionParser);
	}
}
