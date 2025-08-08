package com.nosliw.core.application.common.scriptexpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPExpressionData;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.common.withvariable.HAPPluginProcessorEntityWithVariable;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorEntityWithVariableScriptExpression implements HAPPluginProcessorEntityWithVariable{

	public static String RESULT = "result";
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	public HAPPluginProcessorEntityWithVariableScriptExpression(HAPRuntimeEnvironment runtimeEnv, HAPManagerWithVariablePlugin withVariableMan) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_withVariableMan = withVariableMan;
	}
	
	@Override
	public String getEntityType() {
		return HAPConstantShared.WITHVARIABLE_ENTITYTYPE_SCRIPTEXPRESSION;
	}

	@Override
	public Set<String> getVariableKeys(Object withVariableEntity){
		Set<String> out = new HashSet<String>();
		HAPManualExpressionScript scriptExpression = (HAPManualExpressionScript)withVariableEntity;

		HAPManualUtilityScriptExpressionTraverse.traverse(scriptExpression, new HAPManualProcessorScriptExpressionSegment() {
			@Override
			public boolean process(HAPManualSegmentScriptExpression segment, Object value) {
				Set<String> varKeys = (Set<String>)value;
				String segType = segment.getType();
				if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
					HAPManualSegmentScriptExpressionScriptSimple simpleScriptSeg = (HAPManualSegmentScriptExpressionScriptSimple)segment;
					varKeys.addAll(simpleScriptSeg.getVariableKeys());
				}
				else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
					HAPManualSegmentScriptExpressionScriptDataExpression dataExpressionSeg = (HAPManualSegmentScriptExpressionScriptDataExpression)segment;
					HAPExpressionData dataExpression = scriptExpression.getDataExpressionContainer().getItem(dataExpressionSeg.getDataExpressionId()).getDataExpression();
					varKeys.addAll(HAPUtilityWithVarible.getVariableKeys(dataExpression, m_withVariableMan));
				}
				return true;
			}
		}, out);
		return out;
	}

	
	@Override
	public void resolveVariable(Object withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure) {
		HAPManualExpressionScript scriptExpression = (HAPManualExpressionScript)withVariableEntity;

		HAPManualUtilityScriptExpressionTraverse.traverse(scriptExpression, new HAPManualProcessorScriptExpressionSegment() {
			@Override
			public boolean process(HAPManualSegmentScriptExpression segment, Object value) {
				Set<String> varKeys = (Set<String>)value;
				String segType = segment.getType();
				if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
					HAPManualSegmentScriptExpressionScriptSimple simpleScriptSeg = (HAPManualSegmentScriptExpressionScriptSimple)segment;
					for(Object part : simpleScriptSeg.getParts()) {
						if(part instanceof HAPManualVariableInScript) {
							HAPManualVariableInScript varPart = (HAPManualVariableInScript)part;
							String variableKey = varInfoContainer.addVariable(varPart.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, resolveConfigure);
							varPart.setVariableKey(variableKey);
						}
					}
				}
				else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
					HAPManualSegmentScriptExpressionScriptDataExpression dataExpressionSeg = (HAPManualSegmentScriptExpressionScriptDataExpression)segment;
					HAPExpressionData dataExpression = scriptExpression.getDataExpressionContainer().getItem(dataExpressionSeg.getDataExpressionId()).getDataExpression();
					HAPUtilityWithVarible.resolveVariable(dataExpression, varInfoContainer, resolveConfigure, m_withVariableMan);
				}
				return true;
			}
		}, null);
	}

	@Override
	public Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverVariableCriteria(
			Object withVariableEntity, Map<String, HAPDataTypeCriteria> expections,
			HAPContainerVariableInfo varInfoContainer) {
		HAPManualExpressionScript scriptExpression = (HAPManualExpressionScript)withVariableEntity;

		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		List<HAPContainerVariableInfo> varInfoContainerWrapper = new ArrayList<HAPContainerVariableInfo>();
		varInfoContainerWrapper.add(varInfoContainer);
		
		HAPManualUtilityScriptExpressionTraverse.traverse(scriptExpression, new HAPManualProcessorScriptExpressionSegment() {
			@Override
			public boolean process(HAPManualSegmentScriptExpression segment, Object value) {
				List<HAPContainerVariableInfo> varInfoContainerWrapper = (List<HAPContainerVariableInfo>)value; 
				String segType = segment.getType();
				if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
					HAPManualSegmentScriptExpressionScriptSimple simpleScriptSeg = (HAPManualSegmentScriptExpressionScriptSimple)segment;
				}
				else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
					HAPManualSegmentScriptExpressionScriptDataExpression dataExpressionSeg = (HAPManualSegmentScriptExpressionScriptDataExpression)segment;
					HAPExpressionData dataExpression = scriptExpression.getDataExpressionContainer().getItem(dataExpressionSeg.getDataExpressionId()).getDataExpression();
					Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> resolvePair = HAPUtilityWithVarible.discoverVariableCriteria(dataExpression, expections, varInfoContainerWrapper.get(0), m_withVariableMan);
					varInfoContainerWrapper.add(0, resolvePair.getLeft());
					matchers.putAll(resolvePair.getRight());
				}
				return true;
			}
		}, varInfoContainerWrapper);
		return Pair.of(varInfoContainerWrapper.get(0), matchers);
	}

}
