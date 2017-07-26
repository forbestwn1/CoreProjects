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
		requestDiscoverResources : function(objResourceIds, handlers){
			var parms = node_createServiceParms();
			parms.addParm("resourceIds", objResourceIds);
			var service = node_ServiceInfo("requestDiscoverResources", parms);
			
			var remoteServiceTask = new node_RemoteServiceTask("gateway", service, {
				success : function(request, resourceInfos){
					handlers.success.call(request, resourceInfos);
				},
				error : function(){
					
				},
				exception : function(){
					
				}
			}, undefined, undefined);
			
			nosliw.runtime.getRemoteService().addServiceTask(remoteServiceTask);
		},
		
		/**
		 * Callback method used to request to discover resources and load into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered and loaded resource info)
		 */
		requestDiscoverAndLoadResources : function(objResourceIds, handlers){
			
		},
		
		/**
		 * Callback method used to request to load resources into runtime env
		 * @param objResourcesInfo: a list of resource info 
		 * @param callBackFunction (nothing)
		 */
		requestLoadResources : function(resourcesInfo, handlers){
			
			var remoteServiceTask = new node_RemoteServiceTask("gateway", service, {
				success : function(data){
					_.each(data, function(resourceLoaded){
						var resourceInfo = resourceLoaded.resourceInfo;
						var resource = resourceLoaded.resource;
						switch(resource.type)
						{
						case library:
							break
						default : 
							break;
						}
					}, this);
				},
				error : function(){
					
				},
				exception : function(){
					
				}
			}, requestInfo, setting);
			
			nosliw.runtime.getRemoteService().addServiceTask(remoteServiceTask);
			
		},
	};
}();	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});

nosliw.registerSetNodeDataEvent("gateway", function(){
	node_createConfigures({
		url : "gateway",
		contentType: "application/json; charset=utf-8"
	});
	
	nosliw.runtime.getRemoteService().registerSyncTaskConfigure("gateway");
});

	
//Register Node by Name
packageObj.createChildNode("gateway", node_gateway); 
	
})(packageObj);
