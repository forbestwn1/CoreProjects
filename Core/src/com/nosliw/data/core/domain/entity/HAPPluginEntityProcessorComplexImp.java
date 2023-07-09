package com.nosliw.data.core.domain.entity;

public abstract class HAPPluginEntityProcessorComplexImp implements HAPPluginEntityProcessorComplex{

	private Class<? extends HAPExecutableEntityComplex> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginEntityProcessorComplexImp(String entityType, Class<? extends HAPExecutableEntityComplex> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}

	@Override
	public String getEntityType() {    return this.m_entityType;    }
	
	@Override
	public HAPExecutableEntityComplex newExecutable() {
		HAPExecutableEntityComplex out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
			out.setEntityType(this.getEntityType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
	
}
