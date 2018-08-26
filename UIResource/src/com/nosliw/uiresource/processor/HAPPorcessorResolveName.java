package com.nosliw.uiresource.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionScriptSegment;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpressionUtility;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPUIEmbededScriptExpressionInContent;

public class HAPPorcessorResolveName {

	//resolve the name 
	public static void resolve(HAPExecutableUIUnit exeUnit) {
		
		//embeded script in context
		for(HAPUIEmbededScriptExpressionInContent embededContent : exeUnit.getScriptExpressionsInContent()) {
			
			
			
			
			exeUnit.addScriptExpressionsInContent(new HAPUIEmbededScriptExpressionInContent(embededContent.getUIId(), HAPScriptExpressionUtility.toExeEmbedElement(embededContent.getElements())));
		}
		
		
		
		
	}
	

	private static void resolveEmbededScriptExpression(HAPEmbededScriptExpression embedScriptExpression, HAPContextGroup context) {
		for(HAPScriptExpression scriptExpression : embedScriptExpression.getScriptExpressionsList()) {
			for(HAPScriptExpressionScriptSegment scriptSeg : scriptExpression.getScriptSegments()) {
				Map<String, String> varMapping = new LinkedHashMap<String, String>();
				for(String varName : scriptSeg.getVariableNames()) {
					HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(varName), context, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_BEST);
					varMapping.put(varName, resolveInfo.parentNodeId.getFullName());
				}
			}
		}
	}
	
}
