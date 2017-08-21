package com.nosliw.data.core.expression;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * Store information about how to match one data type to some criteria 
 */
public class HAPMatcher1 extends HAPSerializableImp{

	private HAPDataTypeCriteria m_targetCriteria;
	
	private HAPDataTypeId m_sourceDataTypeId;
	
	private HAPRelationship m_relationship;
	
	public HAPMatcher1(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	public HAPMatcher1(HAPDataTypeCriteria targetCriteria, HAPDataTypeId sourceDataTypeId){
		this.m_sourceDataTypeId = sourceDataTypeId;
		this.m_targetCriteria = targetCriteria;
	}
	
	public HAPDataTypeCriteria getTargetCriteria(){  return this.m_targetCriteria;  }
	
	public HAPDataTypeId getSourceDataTypeId(){  return this.m_sourceDataTypeId;   }

	public HAPRelationship getRelationship(){  return this.m_relationship;  }
	
	@Override
	protected String buildLiterate(){
		return null;
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		return true;
	}
	
}
