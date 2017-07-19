//
var libResources = [
	"nosliw.core",
	"external.Underscore;1.6.0",
	"external.Backbone;1.1.2",
	"external.log4javascript;1.0.0",
	"nosliw.constant",
	"nosliw.common",
	"nosliw.expression",
	"nosliw.request",
	"nosliw.id",
	"nosliw.init",
	"nosliw.logging",
	"nosliw.resource",
	"nosliw.remoteservice",
	"nosliw.runtime",
	"nosliw.runtimebrowser"
];

gateway.requestLoadLibraryResources(libResources, function(){
	
	  nosliw.createNode(nosliw.getNodeData("constant.COMMONCONSTANT").RUNTIME_LANGUAGE_JS_GATEWAY, gateway);  

	  
	  nosliw.runtimeName = "browser";

	  nosliw.initModules();

	  var runtimeRhino = nosliw.getNodeData("runtime.createRuntime")(nosliw.runtimeName);

	  nosliw.runtime = runtimeRhino;


	  runtimeRhino.interfaceObjectLifecycle.init();

	  nosliw.generateId = runtimeRhino.getIdService().generateId;
	  
	  var event = new CustomEvent("runtime.start", { "detail": "Example of an event" });
	  document.dispatchEvent(event);
	  
});



