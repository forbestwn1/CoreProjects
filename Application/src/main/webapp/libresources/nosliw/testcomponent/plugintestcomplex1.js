//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_createErrorData;
	var node_componentUtility;
	var node_requestServiceProcessor;
	var node_getObjectType;
	var node_complexEntityUtility;
	var node_basicUtility;
	var node_getBasicEntityObjectInterface;
	var node_getEntityObjectInterface;
	var node_taskUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestComplex1Plugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){

			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createTestComplex1ComponentCore(complexEntityDef, configure);
			}, handlers, request);
			
		},
			
	};

	return loc_out;
};

var loc_createTestComplex1ComponentCore = function(complexEntityDef, configure){
	var loc_complexEntityDef = complexEntityDef;
	var loc_envInterface = {};
	var loc_configureValue = node_createConfigure(configure).getConfigureValue();
	var loc_parentView;
	var loc_mainView;
	var loc_childrenViews = {};
	
	var loc_simpleTest1Atts = {};
	
	var loc_init = function(complexEntityDef, configure){
	};
	
	var loc_out = {

		getDataType: function(){    return  "testComplex1";   },
		getConfigure : function(){   return loc_configureValue;    },
		getVariableGroupId : function(){   return loc_variableGroupId;     },

		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			_.each(loc_complexEntityDef.getAllAttributesName(), function(attrName, i){
				if(node_basicUtility.getNosliwCoreName(attrName)==undefined){
					out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(attrName, undefined));
				}
			});
			return out;
		},
		
		updateView : function(view){
			loc_parentView = $(view);
//			loc_mainView = $('<div class="view view-main" style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:black">testComplex</div>');
			loc_mainView = $('<div style="border-width:thick; border-style:solid; border-color:black">testComplex</div>');
			var parentValuePortView = $('<div>valuePortContainerId: '+node_getEntityObjectInterface(loc_out).getInternalValuePortContainer().getId()+"--"+node_getEntityObjectInterface(loc_out).getExternalValuePortContainer().getId()+'</div>');
			loc_mainView.append(parentValuePortView);
			loc_parentView.append(loc_mainView);
			
			//complex children
			loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].processChildren(function(child){
				var attrName = child.getChildName();
				
				var childCoreEntity = node_complexEntityUtility.getBrickNode(child).getChildValue().getCoreEntity();
				var internalChildValuePortContainer = node_getEntityObjectInterface(childCoreEntity).getInternalValuePortContainer();
				var externalChildValuePortContainer = node_getEntityObjectInterface(childCoreEntity).getExternalValuePortContainer();
				
				//not nosliw attribute
				var rootViewWrapper = $('<div style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:green"/>');
				var attributeView = $('<div>childAttr: '+attrName+'</div>');
				var valuePortView = $('<div>valuePortContainerId: '+internalChildValuePortContainer.getId()+"--"+externalChildValuePortContainer.getId()+'</div>');
				attributeView.append(valuePortView);
				

				//adapter view
				var childEntityCore = node_complexEntityUtility.getCoreEntity(child.getChildValue());
				var childEntityType = node_getComponentInterface(childEntityCore).getDataType();
				if(childEntityType[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_BRICKTYPE]==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TASKWRAPPER){
					//for task wrapper attribute
					var childEntityDef = node_getBasicEntityObjectInterface(childEntityCore).getEntityDefinition();
					var adapterNames = childEntityDef.getAttribute(node_COMMONATRIBUTECONSTANT.BLOCKTASKWRAPPER_TASK).getAdapterNames();
					_.each(adapterNames, function(adapterName, i){
						var adapterView = $('<button>Execute Adapter : '+adapterName+'</button>');
						attributeView.append(adapterView);
						adapterView.click(function() {
							var taskWrapperExeRequest = node_taskUtility.getExecuteWrapperedTaskWithAdapterRequest(childEntityCore, adapterName);
							node_requestServiceProcessor.processRequest(taskWrapperExeRequest);
						});
					});					
				}
				else{
					if(child.getAdapters!=undefined){
						_.each(child.getAdapters(), function(adapter, adapterName){
							var adapterView = $('<button>Execute Adapter : '+adapterName+'</button>');
							attributeView.append(adapterView);
							adapterView.click(function() {
								var adapterExecuteRequest =  adapter.getExecuteRequest();
								node_requestServiceProcessor.processRequest(adapterExecuteRequest);
							});
						});
					}
				}

				var childView = $('<div style="margin-left:10px;" />');
				attributeView.append(childView);
				rootViewWrapper.append(attributeView);
				loc_mainView.append(rootViewWrapper);
				loc_childrenViews[attrName] = childView;
				
				var childComponentInterface = node_getComponentInterface(child.getChildValue());
				childComponentInterface.updateView(childView);
			});
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
		//lifecycle handler
		getLifeCycleRequest : function(transitName, handlers, request){
			if(!transitName.startsWith("_")){
//				return node_createErrorData("code", "message", "data");
//				var k = aaa.bbb.ccc;
			}
		},
		
		
		getGetStateDataRequest : function(handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				var out = {};
				_.each(loc_simpleTest1Atts, function(simpleTest1Att, name){
					out[name] = simpleTest1Att.callBack(node_CONSTANT.COMPONENT_INTERFACE_GETSTATE);
				});
				return out;
			}, handlers, request);
		},
		getRestoreStateDataRequest : function(stateData, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				_.each(loc_simpleTest1Atts, function(simpleTest1Att, name){
					simpleTest1Att.callBack(node_CONSTANT.COMPONENT_INTERFACE_RESTORESTATE, stateData==undefined?undefined:stateData[name]);
				});
			}, handlers, request);
		},

		
		
		
		//execute command
		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getExecuteNosliwCommandRequest : function(commandName, parm, handlers, requestInfo){   this.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(commandName), parm, handlers, requestInfo);    },

		//get part by id
		getPart : function(partId){ },
		
		//get interface exposed
		getInterface : function(){ return {}; },

		//set state for the component core
		setState : function(state){   },

		//component runtime env
		getRuntimeEnv : function(){   return loc_runtimeEnv;    },
		setRuntimeEnv : function(runtimeEnv){   loc_runtimeEnv = runtimeEnv;     },
		
		//value by name
		getValue : function(name){},
		setValue : function(name, value){},
		
		setLifeCycleStatus : function(status){},
		
		//call back when core embeded into runtime during init phase
		getUpdateRuntimeRequest : function(runtimeEnv, handlers, request){},
		
		registerEventListener : function(listener, handler, thisContext){  },
		unregisterEventListener : function(listener){ },

		registerValueChangeEventListener : function(listener, handler, thisContext){   },
		unregisterValueChangeEventListener : function(listener){ },
		
		startLifecycleTask : function(){},
		endLifecycleTask : function(){},
			
	};
	
	loc_init(complexEntityDef, configure);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.createErrorData", function(){node_createErrorData = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTestComplex1Plugin", node_createTestComplex1Plugin); 

})(packageObj);
