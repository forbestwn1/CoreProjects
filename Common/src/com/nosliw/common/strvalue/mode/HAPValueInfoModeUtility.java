package com.nosliw.common.strvalue.mode;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoMode;

public class HAPValueInfoModeUtility {

	public static void loadValueInfos(){
		Set<String> files = new HashSet<String>();
		files.add("mode_basic.xml");
		files.add("mode_entity.xml");
		files.add("mode_map.xml");
		files.add("mode_list.xml");
		files.add("mode_entityoptions.xml");

		HAPValueInfoManager.getInstance().importFromXML(HAPValueInfoMode.class, files);
	}
	
}
