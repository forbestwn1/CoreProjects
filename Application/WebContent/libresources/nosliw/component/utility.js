//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_getComponentLifecycleInterface;
	var node_createConfigure;
	var node_basicUtility;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_getBasicEntityObjectInterface;
	var node_createServiceRequestInfoSimple;
	var node_getEntityTreeNodeInterface;

//*******************************************   Start Node Definition  ************************************** 	
	
var node_componentUtility = {
	
	getInterfaceCallRequest : function(baseObject, getInterfaceFun, interfaceMethodName, argus, handlers, request){
		var flagName = "done_"+interfaceMethodName;
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo(interfaceMethodName, {}), handlers, request);

		//process it if needed
		var processed = false;
		var basicEntityInterface = node_getBasicEntityObjectInterface(baseObject);
		if(basicEntityInterface!=undefined){
			processed = basicEntityInterface.getExtraData(flagName);
		}
		
		if(!(processed==true)){
			//if not processed, then process it
			if(baseObject[interfaceMethodName]!=undefined){
				out.addRequest( baseObject[interfaceMethodName].apply(baseObject, argus));
			}
		}
		
		//after processing, mark processed
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var basicEntityInterface = node_getBasicEntityObjectInterface(baseObject);
			if(basicEntityInterface!=undefined){
				basicEntityInterface.setExtraData(flagName, true);
			}

			var processChildRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
			//process every children
			var treeNodeInterface = node_getEntityTreeNodeInterface(baseObject);
			if(treeNodeInterface!=undefined){
				var childrenName = treeNodeInterface.getChildrenName();
				_.each(childrenName, function(childName, i){
					var childInterface = getInterfaceFun(treeNodeInterface.getChild(childName).getChildValue());
					processChildRequest.addRequest(childInterface[interfaceMethodName].apply(childInterface, Array.apply(null, Array(argus.length)).map(function () {})));
				})
			}
			return processChildRequest;
		}));

		return out;
	},

	makeNewRuntimeContext : function(oldRuntimeContext, override){
		return _.extend({}, oldRuntimeContext, override);
	},

	makeChildRuntimeContext : function(currentRuntimeContext, childId, view){
		var newRuntimeContext = {
//			backupState : currentRuntimeContext.backupState.createChildState(childId),
//			lifecycleEntity : currentRuntimeContext.lifecycleEntity.createChild(childId)
		};
		if(view!=undefined)  newRuntimeContext.view = view;
		
		return this.makeNewRuntimeContext(currentRuntimeContext, newRuntimeContext); 
	},

	isActive : function(status){  return status==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE;    },

	//process runtime configure to figure out
	//    configure for runtime itself
	//    decoration configure
	processRuntimeConfigure : function(configure){
		configure = node_createConfigure(configure);
		var decorationInfos = [];
		var decorationConfigureName = node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_DECORATION);
		var coreConfigureName = node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_CORE);
		if(configure.isChildExist(decorationConfigureName) || configure.isChildExist(coreConfigureName)){
			var coreConfigure = configure.getChildConfigure(coreConfigureName);
			var decsConfigure = configure.getChildConfigure(decorationConfigureName);

			var childConfigureSet = decsConfigure.getChildConfigureSet();
			_.each(childConfigureSet, function(childConfigureInfo, index){
				var decConfigure = childConfigureInfo.configure;
				var decInfoConfigureValue = decConfigure.getChildConfigure(node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_DECORATION_INFO)).getConfigureValue();
				var decCoreConfigure = decConfigure.getChildConfigure(node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_DECORATION_CORE));
				decorationInfos.push(new node_DecorationInfo(decInfoConfigureValue.name, decInfoConfigureValue.type, decInfoConfigureValue.id, decCoreConfigure));
			});
		}
		else{
			coreConfigure = configure;
		}
		
		return {
			coreConfigure : coreConfigure,
			decorations : decorationInfos
		};
	},
	
	buildDecorationInfoArrayFromConfigure : function(configure, path, type){
		var out = [ ];
		var idSet = configure.getChildrenIdSet(path);
		_.each(idSet, function(id, index){
			var decConfigure = configure.getChildConfigure(path, id);
			var decConfigureValue = decConfigure.getConfigureValue();
			out.push(new node_DecorationInfo(type, decConfigureValue.id, decConfigureValue.name, decConfigureValue.resource, decConfigure));
		});
		return out;
	},
	
//	buildDecorationInfoArrayFromConfigure : function(configure, path, type){
//		var out = [];
//		var idSet = configure.getChildrenIdSet(path);
//		_.each(idSet, function(id, index){
//			var decConfigure = configure.getChildConfigure(path, id);
//			var decConfigureValue = decConfigure.getConfigureValue();
//			out.push(new node_DecorationInfo(type, decConfigureValue.id, decConfigureValue.name, decConfigureValue.resource, decConfigure));
//		});
//		return out;
//	},
	

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
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.getStateMachineDefinition", function(){node_getStateMachineDefinition = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("complexentity.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("componentUtility", node_componentUtility); 

})(packageObj);
