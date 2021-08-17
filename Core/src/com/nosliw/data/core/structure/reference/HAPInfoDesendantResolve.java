package com.nosliw.data.core.structure.reference;

import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.structure.HAPElementStructure;

public class HAPInfoDesendantResolve {

	//solved element 
	public HAPElementStructure resolvedElement;
	//solved path to element
	public HAPPath solvedPath;
	//unsolved path part
	public HAPPath remainPath = new HAPPath();
	
}
