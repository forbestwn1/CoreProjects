package com.nosliw.data.datatype.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaAnd;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementId;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaElementRange;

public class HAPDataTypeCriteriaWrapperLiterate extends HAPSerializableImp implements HAPDataTypeCriteria{

	private static final String START_AND = "[[";
	private static final String END_AND = "]]";

	private static final String START_OR = "((";
	private static final String END_OR = "))";

	private String m_literateValue;
	
	private HAPDataTypeCriteria m_criteria;
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_LITERATE;	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria) {
		if(this.m_criteria==null){
			this.m_criteria = this.parseLiterate(this.m_literateValue);
		}
		return this.m_criteria.validate(criteria);
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId) {
		if(this.m_criteria==null){
			this.m_criteria = this.parseLiterate(this.m_literateValue);
		}
		return this.m_criteria.validate(dataTypeId);
	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId() {
		if(this.m_criteria==null){
			this.m_criteria = this.parseLiterate(this.m_literateValue);
		}
		return this.m_criteria.getValidDataTypeId();
	}

	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.m_literateValue = literateValue;
		return true;
	}

	@Override
	protected String buildLiterate(){
		if(this.m_literateValue==null){
			HAPSerializeManager.getInstance().toStringValue(this.m_criteria, HAPSerializationFormat.LITERATE);
		}
		return this.m_literateValue;
	}

	private HAPDataTypeCriteria parseLiterate(String criteriaLiterate){
		HAPDataTypeCriteria out = null;
		
		 criteriaLiterate = criteriaLiterate.trim();
		 if(criteriaLiterate.startsWith(START_AND)){
			 out = new HAPDataTypeCriteriaAnd(parseMutiple(criteriaLiterate.substring(START_AND.length(), criteriaLiterate.length()-END_AND.length())));
		 }
		 else if(criteriaLiterate.startsWith(START_OR)){
			 out = new HAPDataTypeCriteriaAnd(parseMutiple(criteriaLiterate.substring(START_OR.length(), criteriaLiterate.length()-END_OR.length())));
		 }
		 else{
			 String[] criteriaParts = criteriaLiterate.split("-");
			 if(criteriaParts.length==1){
				 //id
				 out = new HAPDataTypeCriteriaElementId((HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[0], HAPSerializationFormat.LITERATE));
			 }
			 else{
				 //range
				 HAPDataTypeId from = null;
				 HAPDataTypeId to = null;
				 if(HAPBasicUtility.isStringNotEmpty(criteriaParts[0])){
					 from = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[0], HAPSerializationFormat.LITERATE);
				 }
				 
				 if(HAPBasicUtility.isStringNotEmpty(criteriaParts[1])){
					 to = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), criteriaParts[1], HAPSerializationFormat.LITERATE);
				 }
				 out = new HAPDataTypeCriteriaElementRange(from, to);
			 }
		 }
		 
		 return out;
	}
	
	private List<HAPDataTypeCriteria> parseMutiple(String mutipleLiterate){
		List<HAPDataTypeCriteria> out = new ArrayList<HAPDataTypeCriteria>();
		String[] literates = mutipleLiterate.split(",");
		
		for(String literate : literates){
			out.add(this.parseLiterate(literate));
		}
		return out;
	}

}
