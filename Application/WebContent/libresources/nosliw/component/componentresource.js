//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;
	var node_resourceUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_loadComponentResourceRequest = function(componentInfo, decorationInfo, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteComponentResource"), handlers, request);
	
	var resourceIds = [];
	var componentResourceId;
	var component;
	if(componentInfo.componentResourceId != undefined){
		componentResourceId = {};
		componentResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = componentInfo.componentResourceId; 
		componentResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = componentInfo.type; 
		resourceIds.push(componentResourceId);
	}
	else{
		component = componentInfo;
	}

	var decorationFactoryInfos = [];
	if(decorationInfo!=undefined){
		_.each(decorationInfo.decoration, function(decFacDef, i){
			var decFacInfo = {};
			if(typeof decFacDef == "string"){
				decFacInfo.id = decFacDef;
				decFacInfo.name = decFacDef;
			}
			else{
				decFacInfo.id = decFacDef.id;
				decFacInfo.name = decFacDef.name;
				decFacInfo.coreFun = decFacDef.coreFun;
				if(decFacInfo.name==undefined)  decFacInfo.name = decFacInfo.id;
			}
			decorationFactoryInfos.push(decFacInfo);

			if(decFacInfo.coreFun==undefined){
				decFacInfo.resourceId = {};
				decFacInfo.resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = decFacInfo.id; 
				decFacInfo.resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = decorationInfo.type; 
				resourceIds.push(decFacInfo.resourceId);
			}
		});
	}

	//load ui module resource and env factory resource
	out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceIds, {
		success : function(requestInfo, resourceTree){
			var componentDecorationInfos = requestInfo.getData("decorationFactoryInfos");
			_.each(componentDecorationInfos, function(decFacInfo, i){
				if(decFacInfo.resourceId!=undefined){
					decFacInfo.coreFun = node_resourceUtility.getResourceFromTree(resourceTree, decFacInfo.resourceId).resourceData;
				}
			});
			
			var component = requestInfo.getData("component");
			if(componentResourceId!=undefined)  component = node_resourceUtility.getResourceFromTree(resourceTree, componentResourceId).resourceData;
			
			return {
				component :component,
				decoration : componentDecorationInfos
			};
		}
	}).withData(component, "component").withData(decorationFactoryInfos, "decorationFactoryInfos"));
	return out;
};	
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("loadComponentResourceRequest", node_loadComponentResourceRequest); 

})(packageObj);
