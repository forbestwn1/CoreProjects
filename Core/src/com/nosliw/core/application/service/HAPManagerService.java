package com.nosliw.core.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//service manager, it is used for runtime purpose
@HAPEntityWithAttribute
public class HAPManagerService implements HAPPluginDivision{

	//all service definition
	private Map<String, HAPInfoService> m_servicesInfo;
	
	private Map<String, HAPFactoryService> m_serviceFactorys;
	
	//all service runtime
	private Map<String, HAPInstanceService> m_serviceInstances;
	
	private HAPManagerServiceInterface m_serviceInterfaceMan;
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPManagerService(HAPRuntimeEnvironment runtimeEnv){
		this.m_runtimeEnv = runtimeEnv;
//		this.m_servicesInfo = new LinkedHashMap<String, HAPInfoService>();
		this.m_serviceInstances = new LinkedHashMap<String, HAPInstanceService>();
		this.m_serviceFactorys = new LinkedHashMap<String, HAPFactoryService>();
	}
	
	public HAPManagerServiceInterface getServiceInterfaceManager() { 
		if(this.m_serviceInterfaceMan==null) {
			this.m_serviceInterfaceMan = new HAPManagerServiceInterface(); 
		}
		return this.m_serviceInterfaceMan;     
	}
	
	@Override
	public Set<HAPIdBrickType> getBrickTypes() {  
		Set<HAPIdBrickType> out = new HashSet<HAPIdBrickType>();
		out.add(HAPEnumBrickType.SERVICEINTERFACE_100);
		out.add(HAPEnumBrickType.SERVICEPROFILE_100);
		return out;
	}

	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		HAPIdBrickType brickTypeId = brickId.getBrickTypeId();
		if(brickTypeId.equals(HAPEnumBrickType.SERVICEPROFILE_100)) {
			HAPBundle bundle = new HAPBundle();
			bundle.setMainBrickWrapper(new HAPWrapperBrickRoot(this.getServiceInfo(brickId.getId()).getServiceProfileInfo()));
			return bundle;
		} 
		else if(brickTypeId.equals(HAPEnumBrickType.SERVICEINTERFACE_100)) {
			HAPBundle bundle = new HAPBundle();
			bundle.setMainBrickWrapper(new HAPWrapperBrickRoot(this.getServiceInterfaceManager().getServiceInterface(new HAPIdServcieInterface(brickId.getId()))));
			return bundle;
		}
		return null;
	}

	
	public void registerServiceInfo(HAPInfoService serviceInfo){	this.getAllServicesInfo().put(serviceInfo.getServiceProfileInfo().getId(), serviceInfo);	}
	
	public HAPInfoService getServiceInfo(String id){
		HAPInfoService out = this.getAllServicesInfo().get(id);
//		if(!out.isProcessed()) {
//			out.process(this.m_runtimeEnv);
//		}
		return out;
	}
	
	public List<HAPServiceProfile> queryDefinition(HAPQueryServiceDefinition query){
		List<HAPServiceProfile> out = new ArrayList<HAPServiceProfile>();
		for(String id : this.getAllServicesInfo().keySet()) {
			boolean found = true;
			HAPBlockServiceProfile profileBlock = this.getAllServicesInfo().get(id).getServiceProfileInfo();
			List<String> tags = profileBlock.getTags();
			for(String keyword : query.getKeywords()) {
				if(!tags.contains(keyword)) {
					found = false;
				}
			}
			
			if(found) {
				HAPServiceProfile profile = new HAPServiceProfile();
				profileBlock.cloneToEntityInfo(profile);
				profile.setTags(profileBlock.getTags());
				profile.setDisplayResource(profileBlock.getDisplayResource());
				
				HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(profileBlock.getTaskInterface(), this.m_runtimeEnv.getBrickManager());
				profile.setInterface(taskInterfaceBlock.getValue());
				
				out.add(profile);
			}
		}
		return out;
	}
	
	private Map<String, HAPInfoService> getAllServicesInfo(){
		if(this.m_servicesInfo==null) {
			this.m_servicesInfo = new LinkedHashMap<String, HAPInfoService>();
			List<HAPInfoService> defs = HAPImporterDataSourceDefinition.loadDataSourceDefinition(this.m_runtimeEnv.getBrickManager());
			for(HAPInfoService def : defs) {
				this.registerServiceInfo(def);
			}
		}
		return this.m_servicesInfo;
	}

	
	
	public void registerServiceInstance(String id, HAPInstanceService serviceInstance){		this.m_serviceInstances.put(id, serviceInstance);	}

	public void registerServiceFactory(String name, HAPFactoryService serviceFactory){		this.m_serviceFactorys.put(name, serviceFactory);	}
	
	//service query is used to find service provider
	public HAPResultInteractive execute(HAPQueryService serviceQuery, Map<String, HAPData> parms){
		//get service instance according to serviceId
		HAPInstanceService serviceInstance = this.m_serviceInstances.get(serviceQuery.getServiceId());
		if(serviceInstance==null){
			try{
				//not exists, then create one using factory
				HAPInfoService serviceInfo = this.getServiceInfo(serviceQuery.getServiceId());
				HAPExecutableService serviceExe;
				String imp = serviceInfo.getServiceRuntimeInfo().getImplementation();
				if(imp.contains(".")){
					//it is class name
					serviceExe = (HAPExecutableService)Class.forName(imp).newInstance();
				}
				else{
					//it is factory name
					serviceExe = this.m_serviceFactorys.get(imp).newService(serviceInfo.getServiceProfileInfo());
				}
				serviceInstance = new HAPInstanceService(serviceInfo.getServiceProfileInfo(), serviceExe);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(serviceInstance!=null) {
				this.registerServiceInstance(serviceQuery.getServiceId(), serviceInstance);
			}
		}
		
		//execute service instance
		HAPResultInteractive out = null;
		if(serviceInstance!=null) {
			Map<String, HAPData> serviceParms = new LinkedHashMap<String, HAPData>();
			
			HAPBlockInteractiveInterfaceTask serviceInterface = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(serviceInstance.getDefinition().getTaskInterface(), this.m_runtimeEnv.getBrickManager());
			for(HAPRequestParmInInteractive parm : serviceInterface.getValue().getRequestParms()) {
				String parmName = parm.getId();
				HAPData parmData = null;
				if(parms!=null) {
					parmData = parms.get(parmName);
				}
				if(parmData==null)
				 {
					parmData = parm.getDefaultValue();   //not provide, use default 
				}
				serviceParms.put(parmName, parmData);
			}
			out = serviceInstance.getExecutable().execute(serviceParms);
		}
		return out;
	}

}
