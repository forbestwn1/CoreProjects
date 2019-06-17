//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_appDataService = function(){

	var loc_appDataByName = {};
	
	var loc_updateOwnerInfoWithUserId = function(ownerInfo){
		var userToken = nosliw.runtime.getSecurityService().getToken();
		ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID] = userToken;
		return ownerInfo;
		
	};

	var loc_getGetAppDataRequest = function(ownerInfo, dataName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var ownerInfo = loc_updateOwnerInfoWithUserId(ownerInfo);
		var appData = loc_appDataByName[dataName];
		if(appData!=undefined){
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return appData;
			}));
		}
		else{
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA;
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA_OWNER] = ownerInfo;
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA_DATANAME] = dataName;
			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
				success : function(request, appData){
					loc_appDataByName[dataName] = appData;
				}
			});
			out.addRequest(gatewayRequest);
		}
		return out;
	};

	var loc_getUpdateAppDataRequest = function(ownerInfo, appDataByName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var ownerInfo = loc_updateOwnerInfoWithUserId(ownerInfo);
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_OWNER] = ownerInfo;
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_DATABYNAME] = appDataByName;
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, updatedUserData){
				var updatedDataByName = updatedUserData[node_COMMONATRIBUTECONSTANT.MINIAPPSETTINGDATA_DATABYNAME];
				_.each(updatedDataByName, function(updatedData, name){
					loc_appDataByName[name] = updatedData;
				});
				return updatedDataByName;
			}
		}, request);
		out.addRequest(gatewayRequest);
		return out;
	};
	
	var loc_out = {
			
			getGetAppDataSegmentInfoRequest : function(ownerInfo, dataName, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appData){
						var dataInfo = [];
						_.each(appData, function(dataEle, i){
							out.push(new node_ApplicationDataInfo(dataName, dataEle.id, dataEle.version));
						});
						return dataInfo;
					}
				}));
				return out;
			},	

			getGetAppDataSegmentByIdRequest : function(ownerInfo, dataName, id, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appData){
						var find = _.find(appData, function(dataSeg, index){
							return dataSeg.id==id;
						});
						return appData[find];
					}
				}));
				return out;
			},	

			getAddAppDataSegmentRequest : function(ownerInfo, dataName, index, data, version, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appData){
						var appDataSegment = {
							id : nosliw.generateId(),
							version : version,
							data : data,
						};
						appData.splice(index, 0, appDataSegment);
						var appDataByName = {};
						appDataByName[dataName] = appData;
						return loc_getUpdateAppDataRequest(ownerInfo, appDataByName, {
							success : function(request, appData){
								return appDataSegment;
							}
						});
					}
				}));
			},	
				
			getDeleteAppDataSegmentRequest : function(ownerInfo, dataName, id, handlers, requester_parent){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appData){
						var find = _.find(appData, function(dataSeg, index){
							return dataSeg.id==id;
						});
						appData.splice(i, 1);
						var appDataByName = {};
						appDataByName[dataName] = appData;
						return loc_getUpdateAppDataRequest(ownerInfo, appDataByName, {
							success : function(request){
								return;
							}
						});
					}
				}));
				return out;
			},	

			getUpdateAppDataSegmentRequest : function(ownerInfo, dataName, appDataSegment, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appData){
						var find = _.find(appData, function(dataSeg, index){
							return dataSeg.id==appDataSegment.id;
						});
						appData[find] = appDataSegment;
						var appDataByName = {};
						appDataByName[dataName] = appData;
						return loc_getUpdateAppDataRequest(ownerInfo, appDataByName, {
							success : function(request){
								return appDataSegment;
							}
						});
					}
				}));
				return out;
			},	
	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("appDataService", node_appDataService); 

})(packageObj);
