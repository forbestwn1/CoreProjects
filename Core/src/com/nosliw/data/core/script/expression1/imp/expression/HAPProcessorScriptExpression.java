package com.nosliw.data.core.script.expression1.imp.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionExpressionData;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionExpressionGroup1;
import com.nosliw.data.core.script.expression1.HAPDefinitionScriptEntity;
import com.nosliw.data.core.script.expression1.HAPExecutableScript;
import com.nosliw.data.core.script.expression1.HAPScript;

public class HAPProcessorScriptExpression {

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	public static HAPExecutableScriptEntityExpression process(
			String id,
			HAPDefinitionScriptEntity scriptDef,
			Map<String, Object> constantValues,
			HAPUpdateName name2IdUpdate,
			HAPDefinitionExpressionGroup1 expressionDef
		) {
		HAPExecutableScriptEntityExpression out = new HAPExecutableScriptEntityExpression(id);
		scriptDef.cloneToEntityInfo(out);
		out.addSegments(process(id, scriptDef.getScript(), constantValues, name2IdUpdate, expressionDef));
		return out;
	}
	
	public static List<HAPExecutableScript> process(
			String id,
			HAPScript script,
			Map<String, Object> constantValues,
			HAPUpdateName name2IdUpdate,
			HAPDefinitionExpressionGroup1 expressionGroup
		) {
		List<HAPExecutableScript> out = new ArrayList<HAPExecutableScript>();
		List<HAPScript> scriptSegs = parseScript(script.getScript());
		for(int j=0; j<scriptSegs.size(); j++) {
			HAPScript scriptSeg = scriptSegs.get(j);
			String scriptType = scriptSeg.getType();
			String scriptId = id+"_"+j;
			if(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA.equals(scriptType)) {
				HAPDefinitionExpressionData expressionItem = new HAPDefinitionExpressionData(scriptSeg.getScript());
				expressionItem.setName(scriptId);
				expressionGroup.addEntityElement(expressionItem);
				out.add(new HAPExecutableScriptSegExpression(scriptId, scriptId));
			}
			else if(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT.equals(scriptType)) {
				HAPExecutableScriptSegScript scriptSegExe = new HAPExecutableScriptSegScript(scriptId, scriptSeg.getScript());
				//update with constant value
				scriptSegExe.updateConstantValue(constantValues);
				out.add(scriptSegExe);
			}
		}
		return out;
	}
	
	
	
//	public boolean isDataExpression() {
//		if(this.m_segments.size()==1 && this.m_segments.get(0) instanceof HAPDefinitionDataExpression) return true;
//		return false;
//	}
//	
//	public List<HAPDefinitionDataExpression> getExpressionDefinitions(){
//		List<HAPDefinitionDataExpression> out = new ArrayList<HAPDefinitionDataExpression>();
//		for(Object element : this.m_segments){
//			if(element instanceof HAPDefinitionDataExpression){
//				out.add((HAPDefinitionDataExpression)element);
//			}
//		}
//		return out;
//	}
//
//	public List<HAPScriptInScriptExpression> getScriptSegments(){
//		List<HAPScriptInScriptExpression> out = new ArrayList<HAPScriptInScriptExpression>();
//		for(Object element : this.m_segments){
//			if(element instanceof HAPScriptInScriptExpression){
//				out.add((HAPScriptInScriptExpression)element);
//			}
//		}
//		return out;
//	}
	
	//parse definition to get segments
	private static List<HAPScript> parseScript(String script){
		List<HAPScript> out = new ArrayList<HAPScript>();
		String content = script;
		int i = 0;
		while(HAPUtilityBasic.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				out.add(HAPScript.newScript(content, HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT));
				content = null;
			}
			else if(index!=0){
				//start with text
				out.add(HAPScript.newScript(content.substring(0, index), HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT));
				content = content.substring(index);
			}
			else{
				//start with expression
				int expEnd = content.indexOf(EXPRESSION_TOKEN_CLOSE);
				int expStart = index + EXPRESSION_TOKEN_OPEN.length();
				//get expression element
				String expressionStr = content.substring(expStart, expEnd);
				content = content.substring(expEnd + EXPRESSION_TOKEN_CLOSE.length());
				//build expression definition
				out.add(HAPScript.newScript(expressionStr, HAPConstantShared.EXPRESSION_SEG_TYPE_DATA));
			}
			i++;
		}
		return out;
	}
	
}
