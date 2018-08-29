package com.nosliw.data.core.script.context;

//store result for resolve reference path
public class HAPInfoRelativeContextResolve{
	//which categary parent belong to
	public HAPContextRootNodeId parentNodeId;
	//
	public String path;
	//original refered node
	public HAPContextNode referedNode;
	//unmatched path part
	public String remainPath;
	//apply unmatched path, find the resolvedNode
	public HAPContextNode resolvedNode;
}
