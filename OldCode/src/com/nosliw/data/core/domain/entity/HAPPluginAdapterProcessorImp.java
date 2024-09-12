package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.runtime.HAPExecutable;

public abstract class HAPPluginAdapterProcessorImp implements HAPPluginAdapterProcessor {

	private Class<? extends HAPExecutableEntity> m_exeEntityClass;
	
	private String m_entityType;
	
	public HAPPluginAdapterProcessorImp(String entityType, Class<? extends HAPExecutableEntity> exeEntityClass) {
		this.m_entityType = entityType;
		this.m_exeEntityClass = exeEntityClass;
	}
	
	@Override
	public String getAdapterType() {    return this.m_entityType;    }

	//process definition before value context
	@Override
	public void preProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {}

	//value context extension, variable resolve
	@Override
	public void processValueContextExtension(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {}
	
	//process definition after value context
	@Override
	public void postProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {}

	@Override
	public HAPExecutableEntity newExecutable() {
		HAPExecutableEntity out = null;
		try {
			out = this.m_exeEntityClass.newInstance();
			out.setEntityType(this.getAdapterType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out; 
	}
}
