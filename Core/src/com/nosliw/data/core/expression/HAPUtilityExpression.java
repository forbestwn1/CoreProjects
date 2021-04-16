package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.updatename.HAPUpdateNamePrefix;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityDataComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextStructure;

public class HAPUtilityExpression {

	public static Map<String, HAPData> getDataConstants(HAPDefinitionExpressionGroup expressionGroupDef, HAPContextFlat context){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		for(HAPDefinitionConstant constantDef : getDataConstantsDefinition(expressionGroupDef, context)) {
			HAPData data = constantDef.getData();
			if(data!=null)	out.put(constantDef.getId(), data);
		}
		return out;
	}
	
	public static Set<HAPDefinitionConstant> getDataConstantsDefinition(HAPDefinitionExpressionGroup expressionGroupDef, HAPContextFlat context){
		Set<HAPDefinitionConstant> out = null;
		out = expressionGroupDef.getConstantDefinitions();
		if(out==null) {
			//try to build constant from attachment and context
			if(expressionGroupDef instanceof HAPWithAttachment) {
				out = HAPUtilityDataComponent.buildDataConstantDefinition(((HAPWithAttachment)expressionGroupDef).getAttachmentContainer(), context);
			}
		}
		
		if(out==null)   throw new RuntimeException();
		return out;
	}

	public static HAPContextStructure getContext(Object expressionGroupDef, HAPContext extraContext, HAPRuntimeEnvironment runtimeEnv) {
		return HAPUtilityComponent.getContext(expressionGroupDef, extraContext, HAPUtilityExpressionProcessConfigure.getContextProcessConfigurationForExpression(), runtimeEnv);
	}
	
	//make global name
	public static HAPUpdateName getUpdateNameGlobal(HAPExecutableExpressionGroup expression) {
		return new HAPUpdateNamePrefix(expression.getId()+"_");
	}
	
	//get local name according to global name
	public static String getLocalName(HAPExecutableExpressionGroup expression, String globalName) {
		return globalName.substring((expression.getId()+"_").length());
	}

}
