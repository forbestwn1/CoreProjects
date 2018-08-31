package com.nosliw.uiresource.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.updatename.HAPEntityWithName;
import com.nosliw.common.updatename.HAPUpdateNameMap;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionScriptSegment;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

public class HAPPorcessorResolveName {

	//resolve the name 
	public static void resolve(HAPExecutableUIUnit exeUnit) {
		
		for(HAPUIEmbededScriptExpressionInContent embededContent : exeUnit.getScriptExpressionsInContent()) {	resolveEmbededScriptExpression(embededContent, exeUnit.getContext());	}
		
		for(HAPUIEmbededScriptExpressionInAttribute embededAttribute : exeUnit.getScriptExpressionsInAttribute()) {	resolveEmbededScriptExpression(embededAttribute, exeUnit.getContext());	}
		
		for(HAPUIEmbededScriptExpressionInAttribute embededTagAttribute : exeUnit.getScriptExpressionsInTagAttribute()) {	resolveEmbededScriptExpression(embededTagAttribute, exeUnit.getContext());	}
		
		
	}
	

	private static void resolveEmbededScriptExpression(HAPEmbededScriptExpression embedScriptExpression, HAPContextGroup context) {
		for(HAPScriptExpression scriptExpression : embedScriptExpression.getScriptExpressionsList()) {
			//resolve name in script
			for(HAPScriptExpressionScriptSegment scriptSeg : scriptExpression.getScriptSegments()) {
				resolveEntityWithName(scriptSeg, context);
			}
			
			for(HAPDefinitionExpression expressionDefinition : scriptExpression.getExpressionDefinitions()) {
				resolveEntityWithName(expressionDefinition, context);
			}
		}
	}
	
	private static void resolveEntityWithName(HAPEntityWithName entityWithName, HAPContextGroup context) {
		Map<String, String> varMapping = new LinkedHashMap<String, String>();
		for(String varName : entityWithName.getVariableNames()) {
			HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(varName), context, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_BEST);
			varMapping.put(varName, resolveInfo.contextPath.getFullPath());
		}
		entityWithName.updateVariableNames(new HAPUpdateNameMap(varMapping));

		Map<String, String> constantMapping = new LinkedHashMap<String, String>();
		for(String constantName : entityWithName.getConstantNames()) {
			HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(constantName), context, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_BEST);
			constantMapping.put(constantName, resolveInfo.contextPath.getFullPath());
		}
		entityWithName.updateConstantNames(new HAPUpdateNameMap(constantMapping));
	}
	
	
}
