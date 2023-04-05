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
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestComplex1Plugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			
			var complexEntityCore = loc_createTestComplex1ComponentCore(complexEntityDef, valueContextId, bundleCore, configure);
			
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			_.each(complexEntityDef.getAllAttributesName(), function(attrName, i){
				var attr = complexEntityDef.getAttribute(attrName);
				if(attr.isSimpleAttribute()){
					//normal attribute
					var attrValue = attr.getValue();
					var entityType = attr.getEntityType();
					if(attr.isComplex()==true){
						//complex
						var complexEntityId = attrValue;
						out.addRequest(nosliw.runtime.getPackageService().getCreateComplexEntityRuntimeRequest(complexEntityId, complexEntityCore, bundleCore, complexEntityCore.getConfigure()[attrName]), {
							success : function(request, attrEntity){
								complexEntityCore.setComplexAttribute(attrName, attrEntity);
							}
						});
					}
					else{
						//simple attribute
						if(entityType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1){
							//test_simple attribute
							loc_simpleTest1Atts[attrName] = loc_createSimpleAttribute(nosliw.runtime.getPackageService().createSimpleEntity(attrValue));
						}
					}
				}
				else{
					//container attribute
					if(attr.isElementComplex()==true){
						//complex
						var attrEntity = nosliw.runtime.getPackageService().createContainerComplexEntityRuntime(attr.getContainer(), loc_out, loc_bundleCore, loc_configureValue[attrName]);
						loc_children[attrName] = attrEntity;
					}
					else{
						//simple
						if(attr.getElementEntityType()==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1){
							//test_simple container attribute
							var containerAttr = loc_createContainerAttribute();
							_.each(attr.getElements(), function(ele, i){
								var eleEntity = nosliw.runtime.getPackageService().createSimpleEntity(ele[node_COMMONATRIBUTECONSTANT.ELEMENTCONTAINER_ENTITY][node_COMMONATRIBUTECONSTANT.EMBEDED_VALUE]);
								containerAttr.addElement(eleEntity);
							});
							loc_simpleTest1Atts[attrName] = containerAttr;
						}
					}
				}
			});
			
			return out;
		},
			
	};

	return loc_out;
};

var loc_createSimpleAttribute = function(entity, adapter){
	var loc_entity = entity;
	var loc_adapter = adapter;
	
	var loc_out = {
		getType : function(){   return node_COMMONCONSTANT.ATTRIBUTE_TYPE_SIMPLE;    },
		callBack : function(){
			loc_entity.callBack.apply(this, arguments);
		},
	};
	return loc_out;
};

var loc_createContainerAttribute = function(){
	
	var loc_elements = [];
	
	var loc_out = {
		getType : function(){   return node_COMMONCONSTANT.ATTRIBUTE_TYPE_CONTAINER;    },

		addElement : function(element){
			loc_elements.push(loc_createSimpleAttribute(element));
		},
		
		callBack : function(){
			var argus = arguments;
			_.each(loc_elements, function(ele, i){
				ele.callBack.apply(this, argus);
			});
		},
	};
	return loc_out;
};

var loc_createTestComplex1ComponentCore = function(complexEntityDef, variableGroupId, bundleCore, configure){
	var loc_variableGroupId = variableGroupId;
	var loc_complexEntityDef = complexEntityDef;
	var loc_runtimeInterface;
	var loc_bundleCore = bundleCore;
	var loc_configureValue = node_createConfigure(configure).getConfigureValue();
	var loc_parentView;
	var loc_mainView;
	var loc_children = {};
	var loc_childrenViews = {};
	var loc_stateValueView;
	
	var loc_simpleTest1Atts = {};
	
	var loc_init = function(complexEntityDef, variableGroupId, bundleCore, configure){
		_.each(loc_complexEntityDef.getAllAttributesName(), function(attrName, i){
			var attr = loc_complexEntityDef.getAttribute(attrName);
			if(attr.isSimpleAttribute()){
				//normal attribute
				var attrValue = attr.getValue();
				var entityType = attr.getEntityType();
				if(attr.isComplex()==true){
					//complex
					var complexEntityId = attrValue;
					var attrEntity = nosliw.runtime.getPackageService().createComplexEntityRuntime(complexEntityId, loc_out, loc_bundleCore, loc_configureValue[attrName]);
					loc_children[attrName] = attrEntity;
				}
				else{
					//simple attribute
					if(entityType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1){
						//test_simple attribute
						loc_simpleTest1Atts[attrName] = loc_createSimpleAttribute(nosliw.runtime.getPackageService().createSimpleEntity(attrValue));
					}
				}
			}
			else{
				//container attribute
				if(attr.isElementComplex()==true){
					//complex
					var attrEntity = nosliw.runtime.getPackageService().createContainerComplexEntityRuntime(attr.getContainer(), loc_out, loc_bundleCore, loc_configureValue[attrName]);
					loc_children[attrName] = attrEntity;
				}
				else{
					//simple
					if(attr.getElementEntityType()==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1){
						//test_simple container attribute
						var containerAttr = loc_createContainerAttribute();
						_.each(attr.getElements(), function(ele, i){
							var eleEntity = nosliw.runtime.getPackageService().createSimpleEntity(ele[node_COMMONATRIBUTECONSTANT.ELEMENTCONTAINER_ENTITY][node_COMMONATRIBUTECONSTANT.EMBEDED_VALUE]);
							containerAttr.addElement(eleEntity);
						});
						loc_simpleTest1Atts[attrName] = containerAttr;
					}
				}
			}
		});
	};
	
	var loc_simpleTest1AttrsInvoke = function(funName, parm1, parm2){
		var argus = arguments;
		_.each(loc_simpleTest1Atts, function(simpleTest1Att, name){
			simpleTest1Att.callBack.apply(simpleTest1Att, argus);
		});
	};
	
	var loc_out = {

		getDataType: function(){    return  "testComplex1";   },
		getConfigure : function(){   return loc_configureValue;    },
		getVariableGroupId : function(){   return loc_variableGroupId;     },
		
		getPreInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInitCoreTextComplex", {}), handlers, request);

			//complex attribute
			_.each(loc_children, function(child, childName){
				out.addRequest(child.getPreInitRequest());
			});

			//simpletest1 attribute
			loc_simpleTest1AttrsInvoke(node_CONSTANT.COMPONENT_INTERFACE_PREINIT);
			
			return out;
		},		
		
		//call back to provide runtime context : view (during init phase)
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeContextCoreTextComplex", {}), handlers, request);

			loc_parentView = $(runtimeContext.view);
			loc_mainView = $('<div class="view view-main" style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:black">testComplex</div>');
			loc_parentView.append(loc_mainView);
			
			//complex children
			_.each(loc_children, function(child, childName){
				var rootViewWrapper = $('<div style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:green"/>');
				var attributeView = $('<div>childAttr: '+childName+'</div>');
				var childView = $('<div style="margin-left:10px;" />');
				attributeView.append(childView);
				rootViewWrapper.append(attributeView);
				loc_mainView.append(rootViewWrapper);
				loc_childrenViews[childName] = childView;
				
				var childRuntimeContext = node_componentUtility.makeChildRuntimeContext(runtimeContext, childName, child, childView); 

				
//				var childRuntimeContext = _.extend({}, runtimeContext, {
//					view : childView
//				});

				out.addRequest(child.getUpdateRuntimeContextRequest(childRuntimeContext));
			});
			
			//simpletest1 attribute
			loc_simpleTest1AttrsInvoke(node_CONSTANT.COMPONENT_INTERFACE_UPDATERUNTIMECONTEXT, runtimeContext);
			
			return out;
		},

		getUpdateRuntimeInterfaceRequest : function(runtimeInterface, handlers, request){   
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeInterfaceInComponentRuntime", {}), handlers, request);
			loc_runtimeInterface = runtimeInterface;
			return out;
		},

		getPostInitRequest : function(handlers, request){
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

		setComplexAttribute : function(attrName, attrEntity){
			loc_children[attrName] = attrEntity;
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
	
//	loc_init(complexEntityDef, variableGroupId, bundleCore, configure);
	
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

//Register Node by Name
packageObj.createChildNode("createTestComplex1Plugin2", node_createTestComplex1Plugin); 

})(packageObj);
