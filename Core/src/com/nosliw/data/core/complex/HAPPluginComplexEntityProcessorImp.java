package com.nosliw.data.core.complex;

import java.lang.reflect.Field;

public abstract class HAPPluginComplexEntityProcessorImp implements HAPPluginComplexEntityProcessor{

	private Class<? extends HAPExecutableEntityComplex> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginComplexEntityProcessorImp(Class<? extends HAPExecutableEntityComplex> exeEntityClass) {
		this.m_exeEntityClass = exeEntityClass;
		
		//get entity type from class
		try {
			Field f = this.m_exeEntityClass.getField("ENTITY_TYPE");
			this.m_entityType = (String)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getEntityType() {    return this.m_entityType;    }
	
	@Override
	public HAPExecutableEntityComplex newExecutable() {
		HAPExecutableEntityComplex out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
}
