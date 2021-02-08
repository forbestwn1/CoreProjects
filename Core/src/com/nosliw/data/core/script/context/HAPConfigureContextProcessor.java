package com.nosliw.data.core.script.context;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPConfigureContextProcessor {

	//how to find referenced parent node
	public String relativeResolveMode = HAPConstant.RESOLVEPARENTMODE_BEST;

	//how to handle rule defined in parent node
	public boolean relativeInheritRule = false;
	
	//relative can track to sold parent
	public boolean relativeTrackingToSolid = false;
	
	//context categary to find referenced parent node 
	public String[] parentCategary;
	
	//
	public String inheritMode = HAPConstant.INHERITMODE_CHILD;
	
	public Set<String> inheritanceExcludedInfo;
	
	public HAPConfigureContextProcessor() {	}
	 
	public HAPConfigureContextProcessor cloneConfigure() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		out.relativeResolveMode = this.relativeResolveMode;
		out.relativeInheritRule = this.relativeInheritRule;
		out.relativeTrackingToSolid = this.relativeTrackingToSolid;
		out.inheritMode = this.inheritMode;
		out.parentCategary = this.parentCategary;
		out.inheritanceExcludedInfo = this.inheritanceExcludedInfo;
		return out;
	}
}
