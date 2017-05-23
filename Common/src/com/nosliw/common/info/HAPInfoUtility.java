package com.nosliw.common.info;

public class HAPInfoUtility {

	public static HAPInfo merge(HAPInfo source, HAPInfo hard){
		HAPInfoImpSimple out = new HAPInfoImpSimple();
		
		if(source!=null){
			for(String name : source.getNames())	out.setValue(name, source.getValue(name));
		}

		if(hard!=null){
			for(String name : hard.getNames())   out.setValue(name, hard.getValue(name));
		}
		
		return out;
	}
	
}
