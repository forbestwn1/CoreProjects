package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPManualContextProcess {

	private HAPBundle m_bundle;
	
	private String m_rootBrickName;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	private HAPManagerApplicationBrick m_brickManager;
	
	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerResource m_resourceMan;
	
	private HAPRuntimeInfo m_runtimeInfo;
	
	public HAPManualContextProcess(
			HAPBundle bundle, 
			String rootBrickName, 
			HAPManualManagerBrick manualBrickMan, 
			HAPManagerApplicationBrick brickMan, 
			HAPDataTypeHelper dataTypeHelper, 
			HAPManagerResource resourceMan,
			HAPRuntimeInfo runtimeInfo) {
		this.m_bundle = bundle;
		this.m_rootBrickName = rootBrickName;
		this.m_manualBrickMan = manualBrickMan;
		this.m_brickManager = brickMan;
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_resourceMan = resourceMan;
		this.m_runtimeInfo = runtimeInfo;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }
	
	public String getRootBrickName() {    return this.m_rootBrickName;       }

	public HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }
	
	public HAPManagerApplicationBrick getBrickManager() {   return this.m_brickManager;     }
	
	public HAPDataTypeHelper getDataTypeHelper() {    return this.m_dataTypeHelper;      }
	
	public HAPManagerResource getResourceManager() {    return this.m_resourceMan;    }
	
	public HAPRuntimeInfo getRuntimeInfo() {    return this.m_runtimeInfo;      }

}
