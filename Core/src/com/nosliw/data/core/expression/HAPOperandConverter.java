package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaManager;

public class HAPOperandConverter extends HAPOperandImp{

	private HAPRelationship m_relationship;
	
	public HAPOperandConverter(HAPRelationship relationship, HAPDataTypeCriteriaManager criteriaMan) {
		super(HAPConstant.EXPRESSION_OPERAND_CONVERTOR, criteriaMan);
		this.m_relationship = relationship;
		this.setDataTypeCriteria(new HAPDataTypeCriteriaElementId(this.m_relationship.getTarget(), this.getDataTypeCriteriaManager()));
	}

	@Override
	public HAPDataTypeCriteria discoverVariables(Map<String, HAPDataTypeCriteria> variablesInfo,
			HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context) {
		return this.getDataTypeCriteria();
	}

	@Override
	public HAPDataTypeCriteria normalize(Map<String, HAPDataTypeCriteria> variablesInfo) {
		return this.getDataTypeCriteria();
	}

}
