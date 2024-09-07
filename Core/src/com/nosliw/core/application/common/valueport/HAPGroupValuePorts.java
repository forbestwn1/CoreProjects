package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

//all value ports from same source of entity
@HAPEntityWithAttribute
public class HAPGroupValuePorts extends HAPEntityInfoImp{

	@HAPAttribute
	public static String VALUEPORT = "valuePort";

	@HAPAttribute
	public static String GROUPTYPE = "groupType";


	private List<HAPValuePort> m_valuePorts;
	
	//type of group, used to instantiate value port
	private String m_groupType;
	
	private int m_idIndex = 0;
	
	public HAPGroupValuePorts(String groupType) {
		this.m_groupType = groupType;
		this.m_valuePorts = new ArrayList<HAPValuePort>();
	}
	
	public String getGroupType() {    return this.m_groupType;   }
	
	public void addValuePort(HAPValuePort valuePort) {
		String name = valuePort.getName();
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = this.m_idIndex + "";
			this.m_idIndex++;
			valuePort.setName(name);
		}
		this.m_valuePorts.add(valuePort);
	}
	
	public List<HAPValuePort> getValuePorts(){		return this.m_valuePorts;	}

	//name or type
	public HAPValuePort getValuePort(String name) {
		for(HAPValuePort valuePort : this.m_valuePorts) {
			if(valuePort.getName().equals(name)) {
				return valuePort;
			}
		}
		for(HAPValuePort valuePort : this.m_valuePorts) {
			if(valuePort.getType().equals(name)) {
				return valuePort;
			}
		}
		return null;
	}
	
	public HAPValuePort getValuePort(String name, String ioDirection) {
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = this.getDefaultValuePortName(ioDirection);
		}
		return this.getValuePort(name);
	}
	
	public String getDefaultValuePortName(String ioDirection) {
		HAPValuePort out = null;
		if(ioDirection==null) {
			out = this.m_valuePorts.get(0);
		}
		else {
			for(HAPValuePort valuePort : this.m_valuePorts) {
				if(valuePort.getIODirection().equals(HAPConstantShared.IO_DIRECTION_BOTH)) {
					out = valuePort;
					break;
				}
				else if(!ioDirection.equals(HAPConstantShared.IO_DIRECTION_BOTH)) {
					if(ioDirection.equals(valuePort.getIODirection())) {
						out = valuePort;
						break;
					}
				}
			}
		}
		return out.getName();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(GROUPTYPE, this.m_groupType);
		jsonMap.put(VALUEPORT, HAPManagerSerialize.getInstance().toStringValue(this.m_valuePorts, HAPSerializationFormat.JSON));
	}
	
}
