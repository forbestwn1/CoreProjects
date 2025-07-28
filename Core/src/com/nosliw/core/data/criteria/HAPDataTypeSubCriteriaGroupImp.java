package com.nosliw.core.data.criteria;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;

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

	public HAPDataTypeSubCriteriaGroupImp(boolean anySubCriteria){
		this.setOpen(anySubCriteria);
		this.m_subCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
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

	@Override
	public Set<String> getDefinedSubCriteriaNames(){
		return this.m_subCriterias.keySet();
	}

	protected void setOpen(boolean isOpen){  this.m_isOpen = isOpen;  }
	@Override
	public boolean isOpen(){ return this.m_isOpen;  }
	
	@Override
	public void addSubCriteria(String name, HAPDataTypeCriteria subCriteria){  this.m_subCriterias.put(name, subCriteria);  }
	
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

		if(this.m_isOpen)		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.START_SUBCRITERIA_OPEN));
		else		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.START_SUBCRITERIA_CLOSE));
		
		int i = 0;
		for(String name : this.m_subCriterias.keySet()){
			if(i!=0)   out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.COMMAR));
			out.append(name);
			out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.ASSIGNMENT));
			out.append(HAPManagerSerialize.getInstance().toStringValue(this.m_subCriterias.get(name), HAPSerializationFormat.LITERATE));
			i++;
		}
		
		if(this.m_isOpen)		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.END_SUBCRITERIA_OPEN));
		else		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.END_SUBCRITERIA_CLOSE));

		return out.toString(); 
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeSubCriteriaGroupImp){
			HAPDataTypeSubCriteriaGroupImp group = (HAPDataTypeSubCriteriaGroupImp)obj;
			if(HAPUtilityBasic.isEqualMaps(this.m_subCriterias, group.m_subCriterias)){
				if(this.isOpen()==group.isOpen()){
					out = true;
				}
			}
		}
		return out;
	}
}
