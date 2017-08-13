package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPDataTypeSubCriteriaGroupImp extends HAPSerializableImp implements HAPDataTypeSubCriteriaGroup{

	//all the known sub criteria by name
	private Map<String, HAPDataTypeCriteria> m_subCriterias;
	//whether anyName is possible
	private boolean m_isOpen = false;;
	
	public HAPDataTypeSubCriteriaGroupImp(boolean anySubCriteria, Map<String, HAPDataTypeCriteria> subCriterias){
		this.setOpen(anySubCriteria);
		this.m_subCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
		this.addSubCriterias(subCriterias);
	}
	
	@Override
	public HAPDataTypeCriteria getSubCriteria(String name) {
		HAPDataTypeCriteria criteria = this.m_subCriterias.get(name);
		if(criteria==null && this.m_isOpen){
			criteria = HAPDataTypeCriteriaAny.getCriteria();
		}
		return criteria;
	}

	@Override
	public Set<String> getSubCriteriaNames() {
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_subCriterias.keySet());
		if(this.isOpen())  out.add(ANY);
		return out;
	}

	protected void setOpen(boolean isOpen){  this.m_isOpen = isOpen;  }
	protected boolean isOpen(){ return this.m_isOpen;  }
	
	protected void addSubCriteria(String name, HAPDataTypeCriteria subCriteria){  this.m_subCriterias.put(name, subCriteria);  }
	protected void addSubCriterias(Map<String, HAPDataTypeCriteria> subCriterias){
		if(subCriterias!=null){
			for(String name : subCriterias.keySet()){
				this.addSubCriteria(name, subCriterias.get(name));
			}
		}
	}
	
	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();

		if(this.m_isOpen)		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_SUBCRITERIA_OPEN));
		else		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_SUBCRITERIA_CLOSE));
		
		int i = 0;
		for(String name : this.m_subCriterias.keySet()){
			if(i!=0)   out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.COMMAR));
			out.append(name);
			out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.ASSIGNMENT));
			out.append(HAPSerializeManager.getInstance().toStringValue(this.m_subCriterias.get(name), HAPSerializationFormat.LITERATE));
			i++;
		}
		
		if(this.m_isOpen)		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_SUBCRITERIA_OPEN));
		else		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_SUBCRITERIA_CLOSE));

		return out.toString(); 
	}
	
}
