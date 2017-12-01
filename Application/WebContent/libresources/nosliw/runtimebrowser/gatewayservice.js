//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_createConfigures;
	var node_buildServiceProvider;
	var node_requestUtility;
	var node_createServiceRequestInfoExecutor;
	var node_requestServiceProcessor;
	var node_ServiceInfo;
	var node_ServiceRequestExecuteInfo;
	var node_COMMONATRIBUTECONSTANT;
	var node_RemoteServiceTask;
	var node_createServiceRequestInfoRemote;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_createServiceRequestInfoCommon;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
//*******************************************   Start Node Definition  ************************************** 	
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createGatewayService = function(){
	
	var loc_configureName = "gateway";
	
	var loc_getGatewayObject = function(){
		return nosliw.getNodeData(node_COMMONATRIBUTECONSTANT.RUNTIME_NODENAME_GATEWAY);
	};
	
	nosliw.registerSetNodeDataEvent("runtime", function(){
		//register remote task configure
		var configure = node_createConfigures({
			url : loc_configureName,
//			contentType: "application/json; charset=utf-8"
		});
		
		nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
	});
	
	//load file to html page to execute it
	loc_getLoadResourceFileRequest = function(fileName, handlers, requester_parent){
		var out = node_createServiceRequestInfoCommon(new node_ServiceInfo("LoadResourceFile", {"fileName":fileName}), handlers, requester_parent);		
		out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
			var script = document.createElement('script');
			script.setAttribute('src', requestInfo.getService().parms.fileName);
			script.setAttribute('type', 'text/javascript');
			script.onload = function(){
				requestInfo.executeSuccessHandler(data, thisContext);
			};
			document.getElementsByTagName("head")[0].appendChild(script);
		}, out));

		return out;
	};
	
	loc_getLoadResourceDataRequest = function(dataStr, handlers, requester_parent){
		var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("LoadResourceData", {"dataStr":dataStr}), 
				function(requestInfo){  
					eval(requestInfo.getService().parms.dataStr);  
				}, 
				handlers, requester_parent);
		return out;
	};
	
	var loc_out = {

			getExecuteGatewayCommandRequest : function(gatewayId, command, parms, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RequestGatewayService", {"gatewayId":gatewayId,"command":command,"parms":parms}), handlers, requestInfo);

				var gatewayRemoteServiceRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(gatewayId+";"+command, parms), undefined, {
					success : function(requestInfo, gatewayOutput){
						var gatewayOutputData = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_DATA];
						out.setData("gatewayOutputData", gatewayOutputData);
						var gatewayOutputScripts = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_SCRIPTS];
						var requests = [];
						//process script info output 
						_.each(gatewayOutputScripts, function(scriptInfo, i, list){
							var file = scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_FILE];
							if(file!=undefined)		requests.push(loc_getLoadResourceFileRequest(file));
							else		requests.push(loc_getLoadResourceDataRequest(scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_SCRIPT]));
						});
						if(requests.length>0)  return requests;
					}
				});
				out.addRequest(gatewayRemoteServiceRequest);
				
				out.addPostProcessor({
					success : function(requestInfo){
						return out.getData("gatewayOutputData");
					}
				});
				return out;
			},	
			
			
		getExecuteGatewayCommandRequest1 : function(gatewayId, command, parms, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("RequestGatewayService", {"gatewayId":gatewayId,"command":command,"parms":parms}), handlers, requestInfo);

			var remoteServiceRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(gatewayId+";"+command, parms), undefined, {}, requestInfo);
			out.setDependentService(new node_DependentServiceRequestInfo(remoteServiceRequest, {
				success : function(requestInfo, gatewayOutput){
					var gatewayOutputData = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_DATA];
					var gatewayOutputScripts = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_SCRIPTS];

					//process script info output 
					_.each(gatewayOutputScripts, function(scriptInfo, i, list){
						var file = scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_FILE];
						if(file!=undefined){
							var script = document.createElement('script');
							script.setAttribute('src', file);
							script.setAttribute('type', 'text/javascript');
//							script.onload = callBack;
							document.getElementsByTagName("head")[0].appendChild(script);
						}
						else{
							eval(scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_SCRIPT]);
						}
					});
					return gatewayOutputData;
				}
			}));
			return out;
		},	
			
		executeExecuteGatewayCommandRequest : function(gatewayId, command, parms, requester_parent){
			var requestInfo = this.getExecuteGatewayCommandRequest(gatewayId, command, parms, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo, true);
		},
		
		//execute command directly, no callback needed
		executeGatewayCommand : function(gatewayId, command, parms){
			var gatewayObject = loc_getGatewayObject();
			gatewayObject.executeGateway(gatewayId, command, parms);
		}
		
	};
	
	loc_out = node_buildServiceProvider(loc_out, "gatewayService");
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoExecutor", function(){node_createServiceRequestInfoExecutor = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("remote.entity.RemoteServiceTask", function(){node_RemoteServiceTask = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoRemote", function(){node_createServiceRequestInfoRemote = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createGatewayService", node_createGatewayService); 

})(packageObj);
