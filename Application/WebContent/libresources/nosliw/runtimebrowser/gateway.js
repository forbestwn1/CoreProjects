//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_createConfigures;
//*******************************************   Start Node Definition  ************************************** 	

	
var node_gateway = function(){
	
	
	return {
		getExpression : function(suite, name){
			
		},
		
		/**
		 * Callback method used to request to discover resources into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered resource info)
		 */
		requestDiscoverResources : function(objResourceIds, callBackFunction){
//			nosliw.runtime.getRemoteService().
		},
		
		/**
		 * Callback method used to request to discover resources and load into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered and loaded resource info)
		 */
		requestDiscoverAndLoadResources : function(objResourceIds, callBackFunction){
			
		},
		
		/**
		 * Callback method used to request to load resources into runtime env
		 * @param objResourcesInfo: a list of resource info 
		 * @param callBackFunction (nothing)
		 */
		requestLoadResources : function(resourcesInfo, callBackFunction){
			var scriptType = requestInfo.getParmData('type');
			var scriptInfo = encodeURI(requestInfo.getParmData('info'));

			var url = "http://localhost:8080/Application/loadResource?resources="+resources;

			  var script = document.createElement('script');
			  script.setAttribute('src', url);
			  script.setAttribute('type', 'text/javascript');

			  script.onload = callBackFunction;
			  script.onreadystatechange = callBackFunction;
			  document.getElementsByTagName("head")[0].appendChild(script);
		},
	};
}();	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});

nosliw.registerSetNodeDataEvent("runtime", function(){
	node_createConfigures({
		url : "gateway",
		dataTye : "script",
		contentType: "application/json; charset=utf-8"
	});
	
	
	nosliw.runtime.getRemoteService().registerSyncTaskConfigure("gateway");
});

	
//Register Node by Name
packageObj.createChildNode("gateway", node_gateway); 
	
})(packageObj);
