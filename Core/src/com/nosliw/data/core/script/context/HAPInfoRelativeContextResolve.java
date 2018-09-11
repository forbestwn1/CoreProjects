package com.nosliw.data.core.script.context;

//store result for resolve reference path
public class HAPInfoRelativeContextResolve{
	//parent reference path
	public HAPContextPath path;
	//parent root node
	public HAPContextNodeRoot rootNode;
	//original refered node
	public HAPContextNode referedNode;
	//unmatched path part
	public String remainPath;
	//apply unmatched path, find the resolvedNode
	public HAPContextNode resolvedNode;
}
