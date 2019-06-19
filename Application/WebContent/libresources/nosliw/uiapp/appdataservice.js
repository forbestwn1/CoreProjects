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

	var loc_catchedAppData = {};
	
	var loc_getCachedAppData = function(ownerInfo, dataName){
		var ownerType = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE];
		if(ownerType==undefined)  ownerType = node_COMMONCONSTANT.MINIAPP_DATAOWNER_APP;
		var appDataByOwnerId = loc_catchedAppData[ownerType];
		if(appDataByOwnerId==undefined)   return;
		
		var ownerId = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENID];
		var appDataByName = appDataByOwnerId[ownerId];
		if(appDataByName==undefined)  return;
		
		return appDataByName[dataName];
	};
	
	var loc_updateCachedAppData = function(ownerInfo, dataName, appData){
		var ownerType = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE];
		if(ownerType==undefined)  ownerType = node_COMMONCONSTANT.MINIAPP_DATAOWNER_APP;
		var appDataByOwnerId = loc_catchedAppData[ownerType];
		if(appDataByOwnerId==undefined){
			appDataByOwnerId = {};
			loc_catchedAppData[ownerType] = appDataByOwnerId;
		}
		
		var ownerId = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENID];
		var appDataByName = appDataByOwnerId[ownerId];
		if(appDataByName==undefined){
			appDataByName = {};
			appDataByOwnerId[ownerId] = appDataByName;
		}
		
		var appData = appDataByName[dataName];
		if(appData)
		
		if(appData!=undefined)  appDataByName[dataName] = appData;
		else appDataByName[dataName] = [];
	};
	
	var loc_updateOwnerInfoWithUserId = function(ownerInfo){
		var userToken = nosliw.runtime.getSecurityService().getToken();
		ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID] = userToken;
		return ownerInfo;
		
	};

	var loc_getGetAppDataRequest = function(ownerInfo, dataName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var ownerInfo = loc_updateOwnerInfoWithUserId(ownerInfo);
		
		var dataNames;
		if(dataName!=undefined){
			if(Array.isArray(dataName))   dataNames = dataName;
			else dataNames = [dataName];
		}
		
		var appDataByName = {};

		if(dataNames!=undefined){
			var notCached = [];
			_.each(dataNames, function(name, index){
				var appData = loc_getCachedAppData(ownerInfo, name);
				if(appData!=undefined){
					appDataByName[name] = appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
				}
				else{
					notCached.push(name);
				}
			});
			dataNames = notCached;
		}
		
		if(dataNames!=undefined && dataNames.length==0){
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return appDataByName;
			}));
		}
		else{
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA;
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA_OWNER] = ownerInfo;
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA_DATANAME] = dataNames;

			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
				success : function(request, appDataInfo){
					var dataByName = appDataInfo[node_COMMONATRIBUTECONSTANT.MINIAPPSETTINGDATA_DATABYNAME];
					if(dataNames==undefined){
						_.each(dataByName, function(appData, dataName){
							if(appData==undefined)   appData = [];
							loc_updateCachedAppData(ownerInfo, dataName, appData);
							appDataByName[dataName] = appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
						});
					}
					else{
						_.each(dataNames, function(dataName, index){
							var appData = dataByName[dataName];
							if(appData==undefined){
								appData = {};
								appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA] = [];
								appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_NAME] = dataName;
							}
							loc_updateCachedAppData(ownerInfo, dataName, appData);
							appDataByName[dataName] = appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
						});
					}
					return appDataByName;
				}
			});
			out.addRequest(gatewayRequest);
		}
		
		return out;
	};

	var loc_getUpdateAppDataRequest = function(ownerInfo, dataByName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var ownerInfo = loc_updateOwnerInfoWithUserId(ownerInfo);
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_OWNER] = ownerInfo;
		
		var appDataByName = {};
		_.each(dataByName, function(data, name){
			var appData = loc_getCachedAppData(ownerInfo, name);
			if(appData!=undefined){
				appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA] = data;
			}
			else{
				appData = {};
				appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA] = data;
				appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_NAME] = name;
			}
			appDataByName[name] = appData;
		});
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_DATABYNAME] = appDataByName;

		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, updatedUserData){
				var out = {};
				var updatedDataByName = updatedUserData[node_COMMONATRIBUTECONSTANT.MINIAPPSETTINGDATA_DATABYNAME];
				_.each(updatedDataByName, function(updatedData, name){
					loc_updateCachedAppData(ownerInfo, name, updatedData);
					out[name] = updatedData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
				});
				return out;
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
						_.each(appData[dataName], function(dataEle, i){
							dataInfo.push(new node_ApplicationDataInfo(ownerInfo, dataName, dataEle.id, dataEle.version));
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

			getAddAppDataSegmentRequest : function(ownerInfo, dataName, index, id, data, version, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appDataByName){
						var appDataSegment = {
							id : id,
							version : version,
							data : data,
						};
						appDataByName[dataName].splice(index, 0, appDataSegment);
						return loc_getUpdateAppDataRequest(ownerInfo, appDataByName, {
							success : function(request, appData){
								return appDataSegment;
							}
						});
					}
				}));
				return out;
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
