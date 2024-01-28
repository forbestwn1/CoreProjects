//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ApplicationDataSegmentInfo;
	var node_ApplicationDataSegment;
	var node_basicUtility;
	
//*******************************************   Start Node Definition  ************************************** 	
//helper method to build app data info 
var node_createAppDataInfo = function(ownerInfo, dataName, data, id){
	var loc_out = {};
	
	loc_out[node_COMMONATRIBUTECONSTANT.APPDATAINFO_OWNERINFO] = ownerInfo;
	loc_out[node_COMMONATRIBUTECONSTANT.APPDATAINFO_NAME] = dataName;
	loc_out[node_COMMONATRIBUTECONSTANT.APPDATAINFO_DATA] = data;
	loc_out[node_COMMONATRIBUTECONSTANT.APPDATAINFO_ID] = id;
	
	return loc_out;
};	

//helper to handle app data info container info
var node_createAppDataInfoContainer = function(appDataInfoContainerResponse){
	var loc_appDataInfoContainerResponse = appDataInfoContainerResponse;
	var loc_appDataInfos = appDataInfoContainerResponse[node_COMMONATRIBUTECONSTANT.APPDATAINFOCONTAINER_APPDATAINFOS];
	
	var loc_getAppDataInfo = function(ownerInfo, dataName){
		var out = _.find(loc_appDataInfos, function(appDataInfo){
			var result = false;
			var ownerInfo1 = appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_OWNERINFO];
			if(ownerInfo1[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID]==ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID]){
				if(ownerInfo1[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE]==ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE]){
					if(ownerInfo1[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID]==ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID]){
						if(dataName==appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_NAME]){
							result = true;
						}
					}
				}
			}
			return result;
		});
		return out;
	};
	
	var loc_getAppData = function(ownerInfo, dataName){
		var appDataInfo = loc_getAppDataInfo(ownerInfo, dataName);
		return appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_DATA];
	};
	
	var loc_buildAppDataSegmentInfoArray = function(appData, ownerInfo, dataName){
		var out = [];
		_.each(appData, function(dataEle, i){
			out.push(new node_ApplicationDataSegmentInfo(ownerInfo, dataName, dataEle.id, dataEle.version));
		});
		return out;
	};
	
	var loc_out = {
		
		getAppDataInfoContainerResponse : function(){    return loc_appDataInfoContainerResponse;  },
			
		getAppDataInfos : function(){   return loc_appDataInfos;   },
		
		getAppDataSegmentInfoArray : function(ownerInfo, dataName){
			var appData = loc_getAppData(ownerInfo, dataName);
			return loc_buildAppDataSegmentInfoArray(appData, ownerInfo, dataName);
		},
		
		getAppDataSegmentInfo(ownerInfo, dataName, segId){
			var segs = loc_out.getAppDataSegmentInfoArray(ownerInfo, dataName);
			var seg = _.find(segs, function(seg){
				return seg.id == segId;
			});
			return seg;
		},
		
		getAppDataSegment(ownerInfo, dataName, segId){
			var appData = loc_getAppData(ownerInfo, dataName);
			return _.find(appData, function(dataEle, i){
				return dataEle.id == segId;
			});
		},
		
		addAppDataSegment(ownerInfo, dataName, index, segId, data, version){
			var appData = loc_getAppData(ownerInfo, dataName);
			var appDataSegment = new node_ApplicationDataSegment(data, segId, version);
			appData.splice(index, 0, appDataSegment);
			return index;
		},
		
		updateAppDataSegment(ownerInfo, dataName, segId, data){
			var appData = loc_getAppData(ownerInfo, dataName);
			var findIndex = -1;
			var find = _.find(appData, function(dataSeg, index){
				findIndex = index;
				return dataSeg.id==segId;
			});
			find.data = data;
			return findIndex;
		},
		
		deleteAppDataSegment(ownerInfo, dataName, segId){
			var appData = loc_getAppData(ownerInfo, dataName);
			var findIndex = -1;
			var find = _.find(appData, function(dataSeg, index){
				findIndex = index;
				return dataSeg.id==segId;
			});
			appData.splice(findIndex, 1);
			return findIndex;
		},
	};
	
	return loc_out;
};

var node_appDataService = function(){

	//cached app data, including array of data and data name
	var loc_catchedAppData = {};

	var loc_getCachedAppDataInfo = function(appDataInfo){
		return loc_getCachedAppData(appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_OWNERINFO], appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_NAME]);
	};

	var loc_getCachedAppData = function(ownerInfo, dataName){
		var ownerType = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE];
		if(ownerType==undefined)  ownerType = node_COMMONCONSTANT.MINIAPP_DATAOWNER_APP;
		var appDataByOwnerId = loc_catchedAppData[ownerType];
		if(appDataByOwnerId==undefined)   return;
		
		var ownerId = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID];
		var appDataByName = appDataByOwnerId[ownerId];
		if(appDataByName==undefined)  return;
		
		return node_basicUtility.clone(appDataByName[dataName]);
	};

	var loc_updateCachedAppDataInfo = function(appDataInfo){
		var ownerInfo = appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_OWNERINFO];
		var dataName = appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_NAME];
		
		var ownerType = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE];
		if(ownerType==undefined)  ownerType = node_COMMONCONSTANT.MINIAPP_DATAOWNER_APP;
		var appDataByOwnerId = loc_catchedAppData[ownerType];
		if(appDataByOwnerId==undefined){
			appDataByOwnerId = {};
			loc_catchedAppData[ownerType] = appDataByOwnerId;
		}
		
		var ownerId = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID];
		var appDataByName = appDataByOwnerId[ownerId];
		if(appDataByName==undefined){
			appDataByName = {};
			appDataByOwnerId[ownerId] = appDataByName;
		}
		
		appDataByName[dataName] = node_basicUtility.clone(appDataInfo);
		return appDataInfo;
	};

	//build cached app data and update cache according to appDataContainer gateway response  
	var loc_buildCachedAppDataInfoAccordingToResponse = function(appDataInfoContainerResponse){
		var appDataInfos = appDataInfoContainerResponse[node_COMMONATRIBUTECONSTANT.APPDATAINFOCONTAINER_APPDATAINFOS];
		_.each(appDataInfos, function(appDataInfo, i){
			if(appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_DATA]==undefined)  appDataInfo[node_COMMONATRIBUTECONSTANT.APPDATAINFO_DATA] = [];
			loc_updateCachedAppDataInfo(appDataInfo);
		});
	};

	var loc_updateCachedAppData
	
	var loc_getGetOwnerAppDataRequest = function(ownerInfo, handlers, request){
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETOWNERAPPDATA;
		var parms = {};
		var containerParm = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETOWNERAPPDATA_OWNER] = ownerInfo;

		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, appDataInfoContainerResponse){
				loc_buildCachedAppDataInfoAccordingToResponse(appDataInfoContainerResponse);
				return node_createAppDataInfoContainer(appDataInfoContainerResponse);
			}
		});
		out.addRequest(gatewayRequest);
	};
	
	//get app data according to owner and data name
	//if data name is undefined, then return all app data belong to that owner
	var loc_getGetAppDataRequest = function(appDataInfo, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var cached = [];
		var notCached = [];
		
		var appDataInfos = [];
		if(Array.isArray(appDataInfo))   appDataInfos = appDataInfo;
		else appDataInfos = [appDataInfo];
		_.each(appDataInfos, function(appDataInfo, index){
			var cachedAppDataInfo = loc_getCachedAppDataInfo(appDataInfo);
			if(cachedAppDataInfo!=undefined){
				cached.push(cachedAppDataInfo);
			}
			else{
				notCached.push(appDataInfo);
			}
		});
		
		if(notCached!=undefined && notCached.length==0){
			//all from cache
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				var appDataInfoContainerResponse = {};
				appDataInfoContainerResponse[node_COMMONATRIBUTECONSTANT.APPDATAINFOCONTAINER_APPDATAINFOS] = cached;
				return node_createAppDataInfoContainer(appDataInfoContainerResponse);
			}));
		}
		else{
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA;
			var parms = {};
			var containerParm = {};
			containerParm[node_COMMONATRIBUTECONSTANT.APPDATAINFOCONTAINER_APPDATAINFOS] = notCached;
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA_INFOS] = containerParm;

			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
				success : function(request, appDataInfoContainerResponse){
					loc_buildCachedAppDataInfoAccordingToResponse(appDataInfoContainerResponse);
					_.each(cached, function(appDataInfo, i){
						appDataInfoContainerResponse[node_COMMONATRIBUTECONSTANT.APPDATAINFOCONTAINER_APPDATAINFOS].push(appDataInfo);
					});
					return node_createAppDataInfoContainer(appDataInfoContainerResponse);
				}
			});
			out.addRequest(gatewayRequest);
		}
		return out;
	};

	//update app data according to owner and data by name
	var loc_getUpdateAppDataRequest = function(appDataInfo, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA;
		var parms = {};
		
		var appDataInfos = [];
		if(Array.isArray(appDataInfo))   appDataInfos = appDataInfo;
		else appDataInfos = [appDataInfo];
		var containerParm = {};
		containerParm[node_COMMONATRIBUTECONSTANT.APPDATAINFOCONTAINER_APPDATAINFOS] = appDataInfos;
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_INFOS] = containerParm;

		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, appDataInfoContainerResponse){
				loc_buildCachedAppDataInfoAccordingToResponse(appDataInfoContainerResponse);
				return node_createAppDataInfoContainer(appDataInfoContainerResponse);
			}
		}, request);
		out.addRequest(gatewayRequest);
		return out;
	};
	
	var loc_getUpdateAppDataContainerRequest = function(appDataInfoContainer, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_INFOS] = appDataInfoContainer.getAppDataInfoContainerResponse();
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, appDataInfoContainerResponse){
				loc_buildCachedAppDataInfoAccordingToResponse(appDataInfoContainerResponse);
				return node_createAppDataInfoContainer(appDataInfoContainerResponse);
			}
		}, request);
		out.addRequest(gatewayRequest);
		return out;
	};
	
	var loc_out = {

		getGetOwnerAppDataRequest : function(ownerInfo, handlers, request){
			return loc_getGetOwnerAppDataRequest(ownerInfo, handlers, request);
		},
		
		getGetAppDataRequest : function(appDataInfo, handlers, request){
			return loc_getGetAppDataRequest(appDataInfo, handlers, request);
		},

		getGetAppDataSegmentRequest : function(ownerInfo, dataName, segId, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getGetAppDataRequest(node_createAppDataInfo(ownerInfo, dataName), {
				success : function(request, appDataInfoContainer){
					return appDataInfoContainer.getAppDataSegment(ownerInfo, dataName, segId).data;
				}
			}));
			return out;
		},	
		
		//add app data element to app data array
		getAddAppDataSegmentRequest : function(ownerInfo, dataName, index, segId, data, version, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getGetAppDataRequest(node_createAppDataInfo(ownerInfo, dataName), {
				success : function(request, appDataInfoContainer){
					appDataInfoContainer.addAppDataSegment(ownerInfo, dataName, index, segId, data, version);
					return loc_getUpdateAppDataContainerRequest(appDataInfoContainer, {
						success:function(request, appDataInfoContainer){
							return appDataInfoContainer;
						}
					});
				}
			}));
			return out;
		},	

		//update app data element in app data array
		getUpdateAppDataSegmentRequest : function(ownerInfo, dataName, segmentId, data, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getGetAppDataRequest(node_createAppDataInfo(ownerInfo, dataName), {
				success : function(request, appDataInfoContainer){
					appDataInfoContainer.updateAppDataSegment(ownerInfo, dataName, segmentId, data);
					return loc_getUpdateAppDataContainerRequest(appDataInfoContainer, {
						success:function(request, appDataInfoContainer){
							return appDataInfoContainer;
						}
					});
				}
			}));
			return out;
		},	

		//delete app data element in app data array
		getDeleteAppDataSegmentRequest : function(ownerInfo, dataName, segmentId, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getGetAppDataRequest(node_createAppDataInfo(ownerInfo, dataName), {
				success : function(request, appDataInfoContainer){
					appDataInfoContainer.deleteAppDataSegment(ownerInfo, dataName, segmentId);
					return loc_getUpdateAppDataContainerRequest(appDataInfoContainer, {
						success:function(request, appDataInfoContainer){
							return appDataInfoContainer;
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
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataSegmentInfo", function(){node_ApplicationDataSegmentInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataSegment", function(){node_ApplicationDataSegment = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("appDataService", node_appDataService); 
packageObj.createChildNode("createAppDataInfo", node_createAppDataInfo); 

})(packageObj);
