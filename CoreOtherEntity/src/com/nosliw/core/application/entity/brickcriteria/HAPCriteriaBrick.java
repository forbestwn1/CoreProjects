package com.nosliw.core.application.entity.brickcriteria;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPCriteriaBrick extends HAPSerializableImp{

	public final static String CRITERIATYPE = "criteriaTsype"; 

	public final static String RESTRAIN = "restrain"; 

	private String m_criteriaType;
	
	private List<HAPRestrainBrick> m_restrains;
	
	public HAPCriteriaBrick(String criteriaType) {
		this.m_criteriaType = criteriaType;
		this.m_restrains = new ArrayList<HAPRestrainBrick>();
	}
	
	public String getCriteriaType() {		return this.m_criteriaType;  	}

	public List<HAPRestrainBrick> getRestains(){	return this.m_restrains;	}
	
	public void addRestrain(HAPRestrainBrick restrain) {   this.m_restrains.add(restrain);      }
	
	
	
	
}
