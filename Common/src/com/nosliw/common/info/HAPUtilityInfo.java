package com.nosliw.common.info;

public class HAPUtilityInfo {

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

	public static void hardMerge(HAPInfo base, HAPInfo top) {
		for(String name : top.getNames())   base.setValue(name, top.getValue(name));
	}
	
	public static void softMerge(HAPInfo base, HAPInfo top) {
		for(String name : top.getNames()) {
			if(base.getValue(name)==null) {
				base.setValue(name, top.getValue(name));
			}
		}
	}
	
}
