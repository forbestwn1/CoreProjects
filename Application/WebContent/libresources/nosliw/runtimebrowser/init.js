

//
var resources = [];
resources.push("");

gateway.requestLoadResources(resources, function(){
	  nosliw.createNode(nosliw.getNodeData("constant.COMMONCONSTANT").RUNTIME_LANGUAGE_JS_GATEWAY, gateway);  

	  
	  nosliw.runtimeName = "browser";

	  nosliw.initModules();

	  var runtimeRhino = nosliw.getNodeData("runtime.createRuntime")(nosliw.runtimeName);

	  nosliw.runtime = runtimeRhino;


	  runtimeRhino.interfaceObjectLifecycle.init();

	  nosliw.generateId = runtimeRhino.getIdService().generateId;
});



