package com.nosliw.data.core.runtime.js.util.script;

import com.nosliw.data.core.script.expression.HAPExecutableScript;
import com.nosliw.data.core.script.expression.literate.HAPExecutableScriptLiterate;
import com.nosliw.data.core.script.expression.literate.HAPExecutableScriptSegExpressionScript;
import com.nosliw.data.core.script.expression.literate.HAPExecutableScriptSegText;

public class HAPScriptProcessorLiterate implements HAPScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(
			HAPExecutableScript scriptExe, 
			String funciontParmName,
			String expressionsDataParmName, 
			String constantsDataParmName, 
			String variablesDataParmName) {
		HAPOutputScriptProcessor out = new HAPOutputScriptProcessor();
		
		HAPExecutableScriptLiterate literateScript = (HAPExecutableScriptLiterate)scriptExe;
		StringBuffer funScript = new StringBuffer();
		for(HAPExecutableScript seg : literateScript.getSegments()){
			if(seg instanceof HAPExecutableScriptSegExpressionScript){
				funScript.append(funciontParmName+"[\""+seg.getId()+"\"]("+funciontParmName+", "+expressionsDataParmName+", "+constantsDataParmName+", "+variablesDataParmName+")");
				out.addChildren(seg);
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
