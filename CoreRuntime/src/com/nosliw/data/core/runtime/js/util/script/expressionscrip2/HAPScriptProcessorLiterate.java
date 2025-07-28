package com.nosliw.data.core.runtime.js.util.script.expressionscrip2;

import com.nosliw.data.core.script.expression1.HAPExecutableScript;
import com.nosliw.data.core.script.expression1.imp.literate.HAPExecutableScriptEntityLiterate;
import com.nosliw.data.core.script.expression1.imp.literate.HAPExecutableScriptSegExpressionScript;
import com.nosliw.data.core.script.expression1.imp.literate.HAPExecutableScriptSegText;

public class HAPScriptProcessorLiterate implements HAPScriptProcessor{

	@Override
	public HAPOutputScriptProcessor processor(
			HAPExecutableScript scriptExe, 
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
