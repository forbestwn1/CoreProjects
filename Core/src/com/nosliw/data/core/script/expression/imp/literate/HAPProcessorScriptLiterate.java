package com.nosliw.data.core.script.expression.imp.literate;

import java.util.List;
import java.util.Map;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.expression.HAPDefinitionExpressionGroup1;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;
import com.nosliw.data.core.script.expression.HAPScript;
import com.nosliw.data.core.script.expression.imp.expression.HAPProcessorScriptExpression;

public class HAPProcessorScriptLiterate {

	public static HAPExecutableScriptEntityLiterate process(
		String id,
		HAPDefinitionScriptEntity scriptDef,
		Map<String, Object> constantValues,
		HAPUpdateName name2IdUpdate,
		HAPDefinitionExpressionGroup1 expressionDef
	) {
		HAPExecutableScriptEntityLiterate out = new HAPExecutableScriptEntityLiterate(id);
		scriptDef.cloneToEntityInfo(out);
		List<HAPScript> scriptSegs = HAPUtilityScriptLiterate.parseScriptLiterate(scriptDef.getScript().getScript());
		for(int j=0; j<scriptSegs.size(); j++) {
			HAPScript scriptSeg = scriptSegs.get(j);
			String scriptType = scriptSeg.getType();
			String scriptId = id+"_"+j;
			if(HAPConstantShared.SCRIPT_TYPE_SEG_EXPRESSIONSCRIPT.equals(scriptType)) {
				HAPExecutableScriptSegExpressionScript expressionScriptSegExe = new HAPExecutableScriptSegExpressionScript(scriptId);
				expressionScriptSegExe.addSegments(HAPProcessorScriptExpression.process(scriptId, scriptSeg, constantValues, name2IdUpdate, expressionDef));
				out.addSegment(expressionScriptSegExe);
			}
			else if(HAPConstantShared.SCRIPT_TYPE_SEG_TEXT.equals(scriptType)) {
				out.addSegment(new HAPExecutableScriptSegText(scriptId, scriptSeg.getScript())); 
			}
		}
		return out;
	}
	
}
