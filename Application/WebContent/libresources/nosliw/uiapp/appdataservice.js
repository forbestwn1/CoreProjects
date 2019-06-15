//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_appDataService = function(){
	
	var loc_out = {
			
		getGetAppDataRequest : function(ownerInfo, dataName, handlers, request){
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_RESOURCE;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES;
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES_RESOURCEIDS] = resourceIds;
			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
			
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("DiscoverResources", {"resourcesId":resourceIds}), handlers, requestInfo);
			out.setDependentService(new node_DependentServiceRequestInfo(gatewayRequest));
			return out;
		},	

		getUpdateAppDataRequest : function(ownerInfo, dataName, appData, handlers, request){

		},	

		
		
	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("appDataService", node_appDataService); 

})(packageObj);
