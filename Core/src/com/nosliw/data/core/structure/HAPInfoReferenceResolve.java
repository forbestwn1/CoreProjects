package com.nosliw.data.core.structure;

import com.nosliw.common.path.HAPComplexPath;

//store result for resolve reference path
public class HAPInfoReferenceResolve{
	//parent root node
	public HAPRoot referredRoot;
	//path (root id + path)
	public HAPComplexPath path;

	//real solved
	public HAPInfoDesendantResolve realSolved;
	
	//sold solved
	public HAPInfoDesendantResolve realSolidSolved;
	
	//final referred element (maybe logic element which embeded in real element)
	public HAPElement resolvedElement;
}
