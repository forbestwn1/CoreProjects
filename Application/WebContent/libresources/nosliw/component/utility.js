//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_getComponentLifecycleInterface;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_componentUtility = {
		
	isActive : function(status){  return status==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE;    },
	
	buildDecorationInfoArrayFromConfigure : function(configure, path, type){
		var out = [];
		var idSet = configure.getChildrenIdSet(path);
		_.each(idSet, function(id, index){
			var decConfigure = configure.getChildConfigure(path, id);
			var decConfigureValue = decConfigure.getConfigureValue();
			out.push(new node_DecorationInfo(type, decConfigureValue.id, decConfigureValue.name, decConfigureValue.resource, decConfigure));
		});
		return out;
	},
	
	cloneDecorationInfoArray : function(decInfoArray){
		var out = [];
		_.each(decInfoArray, function(decInfo, index){
			out.push(new node_DecorationInfo(decInfo.type, decInfo.id, decInfo.name, decInfo.resource, decInfo.configure));
		});
		return out;
	},
	
	//process predefined command for component
	getProcessNosliwCommand : function(componentRuntime, command, parms, handlers, request){
		var coreCommandName = node_basicUtility.getNosliwCoreName(command);
		if(coreCommandName!=undefined && coreCommandName.startsWith("lifecycle_")){
			var lifecycleTransit = coreCommandName.substring("lifecycle_".length);
			return node_getComponentLifecycleInterface(componentRuntime).getTransitRequest(lifecycleTransit, handlers, request);
		}
		return null;
	},
	
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("componentUtility", node_componentUtility); 

})(packageObj);
