//

var nosliw = {}; 
nosliw.init = function(serverBase){
	nosliw.serverBase = serverBase==undefined?"":serverBase;

	var libNames1 = [
		"nosliw.core",
	];

	var libNames2 = [
		"nosliw.constant",
		"nosliw.logging",
		"nosliw.common",
		"nosliw.expression",
		"nosliw.request",
		"nosliw.id",
		"nosliw.resource",
		"nosliw.uidata",
		"nosliw.remoteservice",
		"nosliw.error",
		"nosliw.runtime",
		"nosliw.runtimebrowser",
		"nosliw.uiexpression",
		"nosliw.uiresource",
		"nosliw.miniapp",
		"nosliw.runtimebrowsertest",
	];

	
	var requestLoadLibrary = function(libNames, callBackFunction){
		var libResources = [];
		for(var i in libNames){
			libResources.push({
				"id" : libNames[i],
				"type" : "jslibrary"
			});
		}
		requestLoadLibraryResources(libResources, callBackFunction);
	};
	
	
	var requestLoadLibraryResources = function(resourceIds, callBackFunction){
		var data = {
			command : "requestLoadLibraryResources",
			parms : JSON.stringify({
				"resourceIds" : resourceIds
			})
		};
		
		$.ajax({
			url : nosliw.serverBase+"loadlib",
			type : "POST",
			dataType: "json",
			data : data,
			async : true,
			success : function(serviceData, status){
				var result = serviceData.data.data;
				
				//temperary solution for map
//				result.push("https://maps.googleapis.com/maps/api/js?key=AIzaSyBCcQbzlVgvSWsXexpcAMXkjPgtfVbPiBE");
				
				var fileNumber = result.length;
				var count = 0;
				
				
				var loadScriptInOrder = function(){
					var url = result[count];
					
					var script = document.createElement('script');
					script.setAttribute('src', nosliw.serverBase+url);
					script.setAttribute('defer', "defer");
					script.setAttribute('type', 'text/javascript');

					script.onload = callBack;
					document.getElementsByTagName("head")[0].appendChild(script);
				};
				
				var callBack = function(){
					count++;
					if(count==fileNumber){
						callBackFunction.call();
					}
					else{
						loadScriptInOrder();
					}
				};
				
				loadScriptInOrder();
			},
			error: function(obj, textStatus, errorThrown){
			},
		});
	};

	requestLoadLibrary(libNames1, function(){
		//set runtime name first
		nosliw.createNode("runtime.name", "browser");

		requestLoadLibrary(libNames2, function(){
			  nosliw.registerNodeEvent("runtime", "active",
						function(eventName, nodeName) {
					  		$(document).trigger("nosliwActive");
				  		}
			  );
			  var runtime = nosliw.getNodeData("runtime.createRuntime")(nosliw.runtimeName);
			  runtime.interfaceObjectLifecycle.init();
		});
	});
};
