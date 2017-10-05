//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_createConfigures;
var node_ServiceInfo;
var node_RemoteServiceTask;
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var loc_configureName = "gateway";

var node_createGateway = function(){
	
	
	var out = {};
	
		out[node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_GETEXPRESSIONS] = function(expressionsRequest, handlers){
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_GETEXPRESSIONS_EXPRESSIONS] = expressionsRequest; 
			
			var service = new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_GETEXPRESSIONS, parms);
			
			var remoteServiceTask = new node_RemoteServiceTask(loc_configureName, service, {
				success : function(request, expressionResponses){
					handlers.success.call(request, request, expressionResponses);
				},
				error : function(){
					
				},
				exception : function(){
					
				}
			}, undefined, undefined);
			
			nosliw.runtime.getRemoteService().addServiceTask(remoteServiceTask);
		};
		
		/**
		 * Callback method used to request to discover resources into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered resource info)
		 */
		out[node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_DISCOVERRESOURCES] = function(resourceIds, handlers){
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_DISCOVERRESOURCES_RESOURCEIDS] = resourceIds; 
			
			var service = new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_DISCOVERRESOURCES, parms);
			
			var remoteServiceTask = new node_RemoteServiceTask(loc_configureName, service, {
				success : function(request, resourceInfos){
					handlers.success.call(request, request, resourceInfos);
				},
				error : function(){
					
				},
				exception : function(){
					
				}
			}, undefined, undefined);
			
			nosliw.runtime.getRemoteService().addServiceTask(remoteServiceTask);
		};
		
		/**
		 * Callback method used to request to discover resources and load into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered and loaded resource info)
		 */
		out[node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_DISCOVERANDLOADRESOURCES] = function(resourceIds, handlers){
			
		};
		
		/**
		 * Callback method used to request to load resources into runtime env
		 * @param objResourcesInfo: a list of resource info 
		 * @param callBackFunction (nothing)
		 */
		out[node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_LOADRESOURCES] = function(resourcesInfo, handlers){

			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_LOADRESOURCES_RESOURCEINFOS] = resourcesInfo; 
			
			var service = new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.RUNTIMEGATEWAYJS_COMMAND_LOADRESOURCES, parms);
			
			var remoteServiceTask = new node_RemoteServiceTask(loc_configureName, service, {
				success : function(request, scriptInfos){
					
					_.each(scriptInfos, function(scriptInfo, i, list){
						var file = scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_FILE];
						if(file!=undefined){
							
						}
						else{
							eval(scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_SCRIPT]);
						}
					});
					
					handlers.success.call(request, request, scriptInfos);
				},
				error : function(){
					
				},
				exception : function(){
					
				}
			}, undefined, undefined);
			
			nosliw.runtime.getRemoteService().addServiceTask(remoteServiceTask);
		};
		
		return out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("remote.entity.RemoteServiceTask", function(){node_RemoteServiceTask = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});


nosliw.registerSetNodeDataEvent("runtime", function(){
	var configure = node_createConfigures({
		url : loc_configureName,
//		contentType: "application/json; charset=utf-8"
	});
	
	nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
});

	
//Register Node by Name
packageObj.createChildNode("createGateway", node_createGateway); 

})(packageObj);
