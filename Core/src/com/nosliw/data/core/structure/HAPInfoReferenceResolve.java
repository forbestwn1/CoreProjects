package com.nosliw.data.core.structure;

import com.nosliw.common.path.HAPComplexPath;

//store result for resolve reference path
public class HAPInfoReferenceResolve{
	//path (root id + path)
	public HAPComplexPath path;

	//unmatched path part
	public String remainPath;

	//parent root node
	public HAPRoot referredRoot;
	//referred real element 
	public HAPElement referedRealElement;
	//referred solid real structure element
	public HAPElement referedRealSolidElement;
	//referred element (maybe logic element which embeded in real element)
	public HAPElement resolvedNode;
}
