nosliw.createNode("runtime.name", "browser");

nosliw.init = function(configure){

	nosliw.setConfigure(configure);
	
	var libNames = [
//		"external.Underscore;1.9.1",
//		"external.Backbone;1.3.3",
//		"nosliw.core",
		"nosliw.constant",
		"nosliw.logging",
		"nosliw.common",
		"nosliw.data",
		"nosliw.expression",
		"nosliw.activity",
		"nosliw.process",
		"nosliw.request",
		"nosliw.id",
		"nosliw.resource",
		"nosliw.uidata",
		"nosliw.remoteservice",
		"nosliw.error",
		"nosliw.runtime",
		"nosliw.runtimebrowser",
		"nosliw.uiexpression",
		"nosliw.uicommon",
		"nosliw.uitag",
		"nosliw.uinode",
		"nosliw.uipage",
		"nosliw.dataservice",
		"nosliw.component",
		"nosliw.uimodule",
		"nosliw.uiapp",
		"nosliw.iotask",
		"nosliw.statemachine",
		"nosliw.runtimebrowsertest",
		"nosliw.security",
		"nosliw.framework7",
	];

	nosliw.utility.requestLoadLibraryResources(libNames, configure.version, function(){
		  nosliw.registerNodeEvent("runtime", "active",
					function(eventName, nodeName) {
				  		$(document).trigger("nosliwActive");
			  		}
		  );
		  var runtime = nosliw.getNodeData("runtime.createRuntime")(nosliw.runtimeName);
		  runtime.interfaceObjectLifecycle.init();
	});
};
