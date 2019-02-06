package com.nosliw.data.core.service.use;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;

@HAPEntityWithAttribute
public class HAPDefinitionServiceProvider extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String SERVICEID = "serviceId";

	@HAPAttribute
	public static String SERVICEINTERFACE = "serviceInterface";

	private String m_serviceId;
	
	private HAPServiceInterface m_serviceInterface;
	
	public HAPDefinitionServiceProvider() {}
	
	public String getServiceId() {    return this.m_serviceId;    }
	public void setServiceId(String id) {    this.m_serviceId = id;    }
	
	public HAPServiceInterface getServiceInterface() {  return this.m_serviceInterface;  }
	public void setServiceInterface(HAPServiceInterface serviceInterface) {   this.m_serviceInterface = serviceInterface;   }
	public HAPServiceParm getProviderServiceParm(String parmName) {  return this.m_serviceInterface.getParm(parmName); }

	
}
