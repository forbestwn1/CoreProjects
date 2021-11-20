package com.nosliw.data.core.structure.reference;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPReferenceRootInStrucutre;
import com.nosliw.data.core.structure.HAPRootStructure;

//store result for resolve reference path
public class HAPInfoReferenceResolve{
	public String structureId;
	
	//parent root node
	public HAPRootStructure referredRoot;
	//unique reference to root
	public HAPReferenceRootInStrucutre rootReference;
	//path (root id + path)
	public HAPComplexPath path;

	//real solved
	public HAPInfoDesendantResolve realSolved;
	
	//sold solved
	public HAPInfoDesendantResolve realSolidSolved;
	
	//final referred element (maybe logic element which embeded in real element)
	public HAPElementStructure resolvedElement;
}
