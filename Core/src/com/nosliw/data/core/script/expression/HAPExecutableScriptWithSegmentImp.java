package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;

public abstract class HAPExecutableScriptWithSegmentImp extends HAPExecutableScriptImp implements HAPExecutableScriptWithSegment{

	private List<HAPExecutableScript> m_segs;

	//all data variables info in script
	private HAPVariableInfoInStructure m_variableInfos;
	
	//all variables name in script
	private Set<String> m_variables;
	
	//all expression ref id in script
	private Set<String> m_expressionIds;
	
	private Map<String, HAPDefinitionConstant> m_constantDefs;
	
	public HAPExecutableScriptWithSegmentImp(String id) {
		super(id);
		this.m_segs = new ArrayList<HAPExecutableScript>();
	}
	
	@Override
	public void addSegment(HAPExecutableScript segment) {    this.m_segs.add(segment);   }
	@Override
	public void addSegments(List<HAPExecutableScript> segments) {   this.m_segs.addAll(segments);    }
	
	@Override
	public List<HAPExecutableScript> getSegments(){    return this.m_segs;     }

	@Override
	public HAPVariableInfoInStructure discoverVariablesInfo1(HAPExecutableExpressionGroup expressionGroup) {
		if(this.m_variableInfos==null) {
			this.m_variableInfos = new HAPVariableInfoInStructure();
			for(HAPExecutableScript seg : this.m_segs) {
//				HAPContainerVariableCriteriaInfo varInfos = seg.discoverVariablesInfo1(expressionGroup);
//				for(String name : varInfos.keySet()) {
//					HAPUtilityScriptExpression.addVariableInfo(this.m_variableInfos, varInfos.get(name), name);
//				}
			}
		}
		return this.m_variableInfos;  
	}

	@Override
	public Set<String> discoverVariables(HAPExecutableExpressionGroup expressionGroup){
		if(this.m_variables==null) {
			this.m_variables = new HashSet<String>();
			for(HAPExecutableScript seg : this.m_segs) {
				Set<String> vars = seg.discoverVariables(expressionGroup);
				this.m_variables.addAll(vars);
			}
		}
		return this.m_variables;  
	}

	
	@Override
	public Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableExpressionGroup expressionGroup) {
		if(this.m_constantDefs==null) {
			this.m_constantDefs = new LinkedHashMap<String, HAPDefinitionConstant>();
			for(HAPExecutableScript seg : this.m_segs) {
				for(HAPDefinitionConstant constantDef : seg.discoverConstantsDefinition(expressionGroup)) {
					HAPUtilityScriptExpression.addConstantDefinition(this.m_constantDefs, constantDef);
				}
			}
		}
		return new HashSet<HAPDefinitionConstant>(this.m_constantDefs.values());
	}

	@Override
	public Set<String> discoverExpressionReference(HAPExecutableExpressionGroup expressionGroup){
		if(this.m_expressionIds==null) {
			this.m_expressionIds = new HashSet<String>();
			for(HAPExecutableScript seg : this.m_segs) {
				this.m_expressionIds.addAll(seg.discoverExpressionReference(expressionGroup));
			}
		}
		return this.m_expressionIds;
	}

	@Override
	public void updateConstant(Map<String, Object> value) {
		for(HAPExecutableScript seg : this.m_segs) {
			seg.updateConstant(value);
		}
	}
	
	@Override
	public void updateVariableName(HAPUpdateName nameUpdate) {
		for(HAPExecutableScript segment : this.getSegments()) {
			segment.updateVariableName(nameUpdate);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

}
