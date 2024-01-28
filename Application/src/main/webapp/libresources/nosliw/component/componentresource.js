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
	var node_ResourceId;

//*******************************************   Start Node Definition  ************************************** 	
//generic utility method for loading component resource (component itself + decoration)
//parm componentInfo two options: component id or component object (resourceId + type)
//parm decorationDef contains a list of decoration id or decoration obj
//     { type +  decoration  }
//output {  componentResource,  decoration  }
var node_loadComponentResourceRequest = function(componentInfo, decorationDef, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteComponentResource"), handlers, request);
	
	//build resource id to be loaded for component
	var resourceIds = [];
	var componentResourceId;
	var componentResource;
	if(componentInfo.resourceId != undefined){
		//resource id
		componentResourceId = new node_ResourceId(componentInfo.type, componentInfo.resourceId);
		resourceIds.push(componentResourceId);
	}
	else{
		//otherwise, component resource obj
		componentResource = componentInfo;
	}

	//build decorationInfos and resource id for decoration
	var decorationInfos = [];
	if(decorationDef!=undefined){
		_.each(decorationDef.decoration, function(decDef, i){
			//build decoration info
			var decorationInfo = {};
			if(typeof decDef == "string"){
				decorationInfo = new node_DecorationInfo(decorationDef.type, decDef);
			}
			else{
				decorationInfo = decDef;
				decorationInfo.type = decorationDef.type;
			}
			decorationInfos.push(decorationInfo);

			if(decorationInfo.resource==undefined){
				//if no resource in decorationInfo, it means it need to be loaded resource
				var resourceId = new node_ResourceId(decorationDef.type, decorationInfo.id);
				resourceIds.push(resourceId);
			}
		});
	}

	//load ui module resource and env factory resource
	out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceIds, {
		success : function(requestInfo, resourceTree){
			//build loaded decoration
			_.each(decorationInfos, function(decorationInfo, i){
				//build resource in decoration info
				if(decorationInfo.resource==undefined){
					decorationInfo.resource = node_resourceUtility.getResourceFromTree(resourceTree, new node_ResourceId(decorationInfo.type, decorationInfo.id)).resourceData;
				}
			});
			
			//build loaded component resource
			if(componentResourceId!=undefined)  componentResource = node_resourceUtility.getResourceFromTree(resourceTree, componentResourceId).resourceData;
			
			//loaded component and decoration data
			return {
				componentResource :componentResource,
				decoration : decorationInfos
			};
		}
	}).withData(componentResource, "component").withData(decorationInfos, "decorationInfos"));
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
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();});

//Register Node by Name
packageObj.createChildNode("loadComponentResourceRequest", node_loadComponentResourceRequest); 

})(packageObj);
