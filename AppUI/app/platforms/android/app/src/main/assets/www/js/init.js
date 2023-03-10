/**
 * 
 */
var init = function(rootNode, baseServer, callBackFunction){

	//load lib utility function
	var loadLibrary = function(libs, callBackFunction){
		var count = 0;
		var fileNumber = libs.length;

		var loadScriptInOrder = function(){
			var url = libs[count];
			
			var script = document.createElement('script');
			script.setAttribute('src', url);
			script.setAttribute('defer', "defer");
			script.setAttribute('type', 'text/javascript');

			script.onload = callBack;
			document.getElementsByTagName("head")[0].appendChild(script);
		};
		
		var callBack = function(){
			count++;
			if(count==fileNumber){
				if(callBackFunction!=undefined)		callBackFunction.call();
			}
			else{
				loadScriptInOrder();
			}
		};

		loadScriptInOrder();
	};

	//when nosliw active
	$(document).on("nosliwActive", function(){
		//load mini libs
		loadLibrary([
			"js/application/0_package_service.js",
			"js/application/utility.js",
			"js/application/service.js",
			"js/application/application.js",
			"js/application/userapps/userapps.js",
			"js/application/userapps/group.js",
			"js/application/userapps/miniapp.js",
			"js/application/userapps/userinfo.js",
			"js/application/userapps/usermodule.js",
			"js/application/miniapp/miniappmodule.js",
		], function(){
			//create miniapp
			var minapp = nosliw.getNodeData("miniapp.createApplication")();
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

			nosliw.miniapp = minapp;
			nosliw.createNode("miniapp", minapp);
			var out = node_createServiceRequestInfoSequence(undefined, {
				success : function(requestInfo, data){
			  		$(document).trigger("miniappActive");
				}
			});
			var miniappInitRequest = minapp.interfaceObjectLifecycle.initRequest(rootNode, undefined, out);
			out.addRequest(miniappInitRequest);
			nosliw.getNodeData("request.requestServiceProcessor").processRequest(out);
		});
	});

	//nosliw init first
	loadLibrary([
//		baseServer+"libresources/external/log4javascript/1.0.0/log4javascript.js",
		baseServer+"libresources/nosliw/runtimebrowserinit/init.js",
	], function(){
		nosliw.init(baseServer);
	});

};

