package com.nosliw.data.core.domain.entity.expression.script.resource.js;

import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;
import com.nosliw.data.core.script.expression.HAPExecutableScript;
import com.nosliw.data.core.script.expression.imp.literate.HAPExecutableScriptEntityLiterate;
import com.nosliw.data.core.script.expression.imp.literate.HAPExecutableScriptSegExpressionScript;
import com.nosliw.data.core.script.expression.imp.literate.HAPExecutableScriptSegText;

public class HAPScriptProcessorLiterate implements HAPSegmentScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(
			HAPExecutableSegmentExpression scriptExe, 
			String funciontParmName,
			String expressionsDataParmName, 
			String constantsDataParmName, 
			String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		
		HAPExecutableScriptEntityLiterate literateScript = (HAPExecutableScriptEntityLiterate)scriptExe;
		StringBuffer funScript = new StringBuffer();
		for(HAPExecutableScript seg : literateScript.getSegments()){
			if(seg instanceof HAPExecutableScriptSegExpressionScript){
				funScript.append(funciontParmName+"[\""+seg.getId()+"\"]("+funciontParmName+", "+expressionsDataParmName+", "+constantsDataParmName+", "+variablesDataParmName+")");
				out.addChild(seg);
			}
			else if(seg instanceof HAPExecutableScriptSegText){
				HAPExecutableScriptSegText textSeg = (HAPExecutableScriptSegText)seg;
				funScript.append("\"");
				funScript.append(textSeg.getText());
				funScript.append("\"");
			}
			funScript.append("+");
		}
		funScript.append("\"\"");
		out.setFunctionBody(funScript.toString());
		return out;
	}

}
