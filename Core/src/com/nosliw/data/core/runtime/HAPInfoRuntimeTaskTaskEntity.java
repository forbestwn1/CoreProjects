package com.nosliw.data.core.runtime;

import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPInfoRuntimeTaskTaskEntity{

	private HAPExecutableBundle m_bundle;
	
	private HAPIdEntityInDomain m_mainEntityId;
	
	private Class m_outputClass;
	
	public HAPInfoRuntimeTaskTaskEntity(HAPExecutableBundle bundle, HAPIdEntityInDomain mainEntityId, Class outputClass){
		this.m_bundle = bundle;
		this.m_mainEntityId = mainEntityId;
		this.m_outputClass = outputClass;
	}
	
	public HAPExecutableBundle getBundle() {    return this.m_bundle;    }
	
	public HAPIdEntityInDomain getMainEntityId() {    return this.m_mainEntityId;     }
	
	public Class getOutputClass() {    return this.m_outputClass;     }
	
}
