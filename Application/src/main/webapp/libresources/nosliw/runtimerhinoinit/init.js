//set runtime name first
nosliw.createNode("runtime.name", "rhino");

nosliw.registerNodeEvent("runtime.createRuntime", nosliw.NODEEVENT_SETDATA, function(){
	
	  var runtime = nosliw.getNodeData("runtime.createRuntime")("rhino");
	  runtime.interfaceObjectLifecycle.init();

});

