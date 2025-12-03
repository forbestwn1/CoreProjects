package com.nosliw.core.application.division.manual.common.valuecontext;

public class HAPManualInfoValueStructureSorting {

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private HAPManualWrapperStructure m_valueStructure;
	
	private double m_priority;
	
	public HAPManualInfoValueStructureSorting(HAPManualWrapperStructure valueStructure) {
		this.m_valueStructure = valueStructure;
	}

	public HAPManualWrapperStructure getValueStructure() {    return this.m_valueStructure;     }
	
	public double getPriority(){   return this.m_priority;    }
	public void setPriority(double priority) {     this.m_priority = priority;     }

}
