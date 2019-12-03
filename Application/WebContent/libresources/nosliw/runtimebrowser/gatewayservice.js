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
	var loc_getLoadFileRequest = function(fileName, handlers, requester_parent){
		var url = nosliw.utility.buildNosliwUrl(fileName);
		
		
		var out = node_createServiceRequestInfoCommon(new node_ServiceInfo("LoadResourceFile", {"url":url}), handlers, requester_parent);		
		out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
			var script = document.createElement('script');
			script.setAttribute('src', requestInfo.getService().parms.url);
			script.setAttribute('type', 'text/javascript');
			script.onload = function(){
				requestInfo.successFinish(undefined, requestInfo);
			};
			document.getElementsByTagName("head")[0].appendChild(script);
		}, out));

		return out;
	};
	
	var loc_getLoadScriptRequest = function(dataStr, handlers, requester_parent){
		var out = node_createServiceRequestInfoCommon(new node_ServiceInfo("LoadScript", {"dataStr":dataStr}), handlers, requester_parent);		
		out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
			var dataStr = requestInfo.getService().parms.dataStr;
			try{
				eval(dataStr);
				requestInfo.successFinish();
			}
			catch(err){
				alert(dataStr);
				requestInfo.errorFinish(new node_ServiceData(nocd_constant.ERROR_PARSE_JAVASCRIPT, nocd_constant.ERROR_PARSE_JAVASCRIPT, dataStr));
			}
			
		}, out));

		return out;
		
		
//		var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("LoadScript", {"dataStr":dataStr}), 
//				function(requestInfo){  
//					eval(requestInfo.getService().parms.dataStr);  
//				}, 
//				handlers, requester_parent);
//		return out;
	};
	
	var loc_out = {

			getExecuteGatewayCommandRequest : function(gatewayId, command, parms, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RequestGatewayService", {"gatewayId":gatewayId,"command":command,"parms":parms}), handlers, requestInfo);

				var gatewayRemoteServiceRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(gatewayId+";"+command, parms), undefined, {
					success : function(requestInfo, gatewayOutput){
						var gatewayOutputData = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_DATA];
						out.setData(gatewayOutputData, "gatewayOutputData");
						var gatewayOutputScripts = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_SCRIPTS];
						var requests = [];
						//process script info output 
						_.each(gatewayOutputScripts, function(scriptInfo, i, list){
							var file = scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_FILE];
							var script = scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_SCRIPT];
							if(file!=undefined)		requests.push(loc_getLoadFileRequest(file));
							if(script!=undefined)		requests.push(loc_getLoadScriptRequest(script));
						});
						
						requests.push( node_createServiceRequestInfoSimple({}, function(request){  return out.getData("gatewayOutputData")})  );
						
						if(requests.length>0)  return requests;
					}
				});
				out.addRequest(gatewayRemoteServiceRequest);
				
//				out.addPostProcessor({
//					success : function(requestInfo){
//						return out.getData("gatewayOutputData");
//					}
//				});
				return out;
			},	
			
		executeExecuteGatewayCommandRequest : function(gatewayId, command, parms, handlers, requester_parent){
			var requestInfo = this.getExecuteGatewayCommandRequest(gatewayId, command, parms, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		//execute command directly, no callback needed
		executeGatewayCommand : function(gatewayId, command, parms){
			var gatewayObject = loc_getGatewayObject();
			gatewayObject.executeGateway(gatewayId, command, parms);
		}
		
	};
	
	nosliw.createNode(node_COMMONATRIBUTECONSTANT.RUNTIME_NODENAME_GATEWAY, {
		executeGateway : function(gatewayId, command, parms){
			
		}
	});
	
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
