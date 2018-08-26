package com.nosliw.data.core.script.context;

public class HAPInfoRelativeContextResolve{
	//which categary parent belong to
	public HAPContextRootNodeId parentNodeId;
	//original refered node
	public HAPContextNode referedNode;
	//unmatched path part
	public String remainPath;
	//apply unmatched path, find the resolvedNode
	public HAPContextNode resolvedNode;
}
