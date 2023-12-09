package com.nosliw.data.core.runtime;

import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.domain.HAPExecutableBundle;

public class HAPInfoRuntimeTaskTaskEntity{

	private HAPExecutableBundle m_bundle;
	
	private HAPPath m_mainEntityPath;
	
	private Class m_outputClass;
	
	public HAPInfoRuntimeTaskTaskEntity(HAPExecutableBundle bundle, HAPPath mainEntityPath, Class outputClass){
		this.m_bundle = bundle;
		this.m_mainEntityPath = mainEntityPath;
		this.m_outputClass = outputClass;
	}
	
	public HAPExecutableBundle getBundle() {    return this.m_bundle;    }
	
	public HAPPath getMainEntityPath() {    return this.m_mainEntityPath;     }
	
	public Class getOutputClass() {    return this.m_outputClass;     }
	
}
