package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPPackageBrickInBundle extends HAPSerializableImp{

	@HAPAttribute
	public static final String BRICKID = "brickId";

	@HAPAttribute
	public static final String ADAPATERS = "adapters";

	private HAPIdBrickInBundle m_brickId;
	
    private List<String> m_adapterNames;
    
    public HAPPackageBrickInBundle() {
    	this.m_adapterNames = new ArrayList<String>();
    }
    
    
	
	
}
