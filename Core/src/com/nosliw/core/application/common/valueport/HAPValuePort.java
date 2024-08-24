package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPValuePort extends HAPEntityInfoImp{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String IODIRECTION = "ioDirection";

	private List<String> m_valueStructures;

	private String m_type;
	
	private String m_ioDirection; 

	public HAPValuePort(String type, String ioDirection) {
		this.m_type = type;
		this.m_ioDirection = ioDirection;
		this.m_valueStructures = new ArrayList<String>();
	}
	
	public List<String> getValueStructureIds(){    return this.m_valueStructures;     }

	public String getType() {     return this.m_type;    }

	public String getIODirection() {     return this.m_ioDirection;     }
	public void setIODirection(String ioDirection) {   this.m_ioDirection = ioDirection;   }

}
