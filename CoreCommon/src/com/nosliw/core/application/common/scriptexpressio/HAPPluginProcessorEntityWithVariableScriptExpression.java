package com.nosliw.core.application.common.scriptexpressio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPExpressionData;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.common.withvariable.HAPPluginProcessorEntityWithVariable;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@Component
public class HAPPluginProcessorEntityWithVariableScriptExpression implements HAPPluginProcessorEntityWithVariable<HAPExpressionScript>{

	public static String RESULT = "result";
	
	private HAPManagerWithVariablePlugin m_withVariableMan;
	
	public HAPPluginProcessorEntityWithVariableScriptExpression(HAPManagerWithVariablePlugin withVariableMan) {
		this.m_withVariableMan = withVariableMan;
	}
	
	@Override
	public String getEntityType() {
		return HAPConstantShared.WITHVARIABLE_ENTITYTYPE_SCRIPTEXPRESSION;
	}

	@Override
	public Set<String> getVariableKeys(HAPExpressionScript withVariableEntity){
		Set<String> out = new HashSet<String>();
		HAPExpressionScriptImp scriptExpression = (HAPExpressionScriptImp)withVariableEntity;

		HAPUtilityScriptExpressionTraverse.traverse(scriptExpression, new HAPProcessorScriptExpressionSegment() {
			@Override
			public boolean process(HAPSegmentScriptExpression segment, Object value) {
				Set<String> varKeys = (Set<String>)value;
				String segType = segment.getType();
				if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
					HAPSegmentScriptExpressionScriptSimple simpleScriptSeg = (HAPSegmentScriptExpressionScriptSimple)segment;
					varKeys.addAll(simpleScriptSeg.getVariableKeys());
				}
				else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
					HAPSegmentScriptExpressionScriptDataExpression dataExpressionSeg = (HAPSegmentScriptExpressionScriptDataExpression)segment;
					HAPExpressionData dataExpression = scriptExpression.getDataExpressionContainer().getItem(dataExpressionSeg.getDataExpressionId()).getDataExpression();
					varKeys.addAll(HAPUtilityWithVarible.getVariableKeys(dataExpression, m_withVariableMan));
				}
				return true;
			}
		}, out);
		return out;
	}

	
	@Override
	public void resolveVariable(HAPExpressionScript withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure, HAPRuntimeInfo runtimeInfo) {
		HAPExpressionScriptImp scriptExpression = (HAPExpressionScriptImp)withVariableEntity;

		HAPUtilityScriptExpressionTraverse.traverse(scriptExpression, new HAPProcessorScriptExpressionSegment() {
			@Override
			public boolean process(HAPSegmentScriptExpression segment, Object value) {
				Set<String> varKeys = (Set<String>)value;
				String segType = segment.getType();
				if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
					HAPSegmentScriptExpressionScriptSimple simpleScriptSeg = (HAPSegmentScriptExpressionScriptSimple)segment;
					for(Object part : simpleScriptSeg.getParts()) {
						if(part instanceof HAPVariableInScript) {
							HAPVariableInScript varPart = (HAPVariableInScript)part;
							String variableKey = varInfoContainer.addVariable(varPart.getVariableName(), HAPConstantShared.IO_DIRECTION_OUT, resolveConfigure);
							varPart.setVariableKey(variableKey);
						}
					}
				}
				else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
					HAPSegmentScriptExpressionScriptDataExpression dataExpressionSeg = (HAPSegmentScriptExpressionScriptDataExpression)segment;
					HAPExpressionData dataExpression = scriptExpression.getDataExpressionContainer().getItem(dataExpressionSeg.getDataExpressionId()).getDataExpression();
					HAPUtilityWithVarible.resolveVariable(dataExpression, varInfoContainer, resolveConfigure, m_withVariableMan, runtimeInfo);
				}
				return true;
			}
		}, null);
	}

	@Override
	public Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverVariableCriteria(
			HAPExpressionScript withVariableEntity, Map<String, HAPDataTypeCriteria> expections,
			HAPContainerVariableInfo varInfoContainer) {
		HAPExpressionScriptImp scriptExpression = (HAPExpressionScriptImp)withVariableEntity;

		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		List<HAPContainerVariableInfo> varInfoContainerWrapper = new ArrayList<HAPContainerVariableInfo>();
		varInfoContainerWrapper.add(varInfoContainer);
		
		HAPUtilityScriptExpressionTraverse.traverse(scriptExpression, new HAPProcessorScriptExpressionSegment() {
			@Override
			public boolean process(HAPSegmentScriptExpression segment, Object value) {
				List<HAPContainerVariableInfo> varInfoContainerWrapper = (List<HAPContainerVariableInfo>)value; 
				String segType = segment.getType();
				if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE)) {
					HAPSegmentScriptExpressionScriptSimple simpleScriptSeg = (HAPSegmentScriptExpressionScriptSimple)segment;
				}
				else if(segType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION)) {
					HAPSegmentScriptExpressionScriptDataExpression dataExpressionSeg = (HAPSegmentScriptExpressionScriptDataExpression)segment;
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
