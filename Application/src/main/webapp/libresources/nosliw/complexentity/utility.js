//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_basicUtility;
	var node_getObjectType;
	var node_ResourceId;
	var node_resourceUtility;
	var node_createConfigure;
	var node_getEntityTreeNodeInterface;
	var node_namingConvensionUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_complexEntityUtility = function(){

	var loc_traverseNode = function(coreEntity, processorInfo){
		var treeNodeInterface = node_getEntityTreeNodeInterface(coreEntity);
		if(treeNodeInterface!=undefined){
			var childrenNames = treeNodeInterface.getChildrenName();
			_.each(childrenNames, function(childName, i){
				processorInfo.processLeaf(coreEntity, childName);
				
				var childNode = treeNodeInterface.getChild(childName);
				var childValue = childNode.getChildValue();
				var childEntityCore = loc_out.getCoreEntity(childValue);
				loc_traverseNode(childEntityCore, processorInfo);
			});
		}
	};

	var loc_out = {
		
		getBrickNode : function(node){
			if(node_getObjectType(node.getChildValue().getCoreEntity())==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
				return node.getChildValue().getCoreEntity().getMainEntityNode();
			}
			else return node;					
		},
		
		getBrickCoreByRelativePath : function(baseEntityCore, relativePath){
			var hostEntityCore = baseEntityCore;
			if(relativePath!=undefined && relativePath!=""){
				var segs = relativePath.split(node_COMMONCONSTANT.SEPERATOR_LEVEL2);
				_.each(segs, function(seg, i){
					var treeNodeInterface = node_getEntityTreeNodeInterface(hostEntityCore);
					if(seg.startsWith(node_COMMONCONSTANT.NAME_PARENT)) {
						hostEntityCore = treeNodeInterface.getParentCore();
						if(node_getObjectType(hostEntityCore)==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
							//for bundle node
//							hostEntityCore = node_getEntityTreeNodeInterface(hostEntityCore).getParentCore();
						}
					}
					else if(seg.startsWith(node_COMMONCONSTANT.NAME_CHILD)) {
						var childName = seg.substring(node_COMMONCONSTANT.NAME_CHILD.length+node_COMMONCONSTANT.SEPERATOR_LEVEL1.length);
						if(childName.startsWith(node_COMMONCONSTANT.SYMBOL_KEYWORD)){
							childName = childName.substring(node_COMMONCONSTANT.SYMBOL_KEYWORD.length);
						}
						var childTreeNode = treeNodeInterface.getChild(childName)
						hostEntityCore = childTreeNode.getChildValue().getCoreEntity();
						if(node_getObjectType(hostEntityCore)==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
							//for bundle node
//							hostEntityCore = hostEntityCore.getMainEntityCore();
						}
					}
				});		
			}
			return hostEntityCore;
		},

		getDescendantCore : function(entity, path){
			var out = entity;
			var out = node_getObjectType(out)==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTRUNTIME?out.getCorenEntity():out;
			
			var pathSegs;
			if(node_basicUtility.isArray(path)){
				pathSegs = path;
			}
			else{
				if(node_basicUtility.isStringEmpty(path)) return out;
				pathSegs = node_namingConvensionUtility.parsePathInfos(path);
			}
			
			_.each(pathSegs, function(pathSeg, i){
				out = node_getEntityTreeNodeInterface(out).getChild(pathSeg).getChildValue().getCoreEntity();
			})
			return out;
		},
		
		getInitBrickRequest : function(brickCore, view, envInterface){
			return node_getComponentInterface(brickCore).getPreInitRequest({
				success : function(request){
					//try pass envInterface to main entity
					if(envInterface!=undefined){
						var embededInterface = node_getEmbededEntityInterface(brickCore);
						_.each(envInterface, function(interfacee, name){
							embededInterface.setEnvironmentInterface(name, interfacee);
						});
					}

					//update backup state object
//					var backupStateObj = runtimeContext==undefined?undefined:runtimeContext.backupState;
//					if(backupStateObj!=undefined)  node_getComponentInterface(brickCore).updateBackupStateObject(backupStateObj);

					//update lifecycle entity
//					var lifecycleEntity = runtimeContext==undefined?undefined:runtimeContext.lifecycleEntity;
//					if(lifecycleEntity==undefined)  lifecycleEntity = node_createLifeCycleRuntimeContext("application");
//					node_getComponentInterface(application).updateLifecycleEntityObject(lifecycleEntity);

					//update view
					if(view){
						node_getComponentInterface(brickCore).updateView(view);
					}

					return node_getComponentInterface(brickCore).getPostInitRequest({
						success : function(request){
							return brickCore;
						}
					});
				}
			});
			
		},
		
		traverseNode : function(entity, processorInfo){
			var entityCore = node_getObjectType(entity)==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTRUNTIME?entity.getCorenEntity():entity;
			processorInfo.processRoot(entityCore);
			loc_traverseNode(entityCore, processorInfo);
		},
		
		getCoreEntity : function(obj){
			var out = obj;
			var dataType = node_getObjectType(out);
			if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTRUNTIME){
				out = obj.getCoreEntity();
			}
			
			var coreDataType = node_getObjectType(out);
			if(coreDataType==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
				out = out.getMainEntityCore();
			}
			return out;
		},
		
		getComplexCoreEntity : function(parm){
			var dataType = node_getObjectType(parm);
			if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTRUNTIME){
				return this.getComplexCoreEntity(parm.getCoreEntity());
			}
			else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_APPLICATION){
				return this.getComplexCoreEntity(parm.getPackageRuntime());
			}
			else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE){
				return this.getComplexCoreEntity(parm.getMainBundleRuntime());
			}
			else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
				return this.getComplexCoreEntity(parm.getMainEntityRuntime());
			}
			else{
				return parm;
			}
		},
	
		getAttributeAdapterExecuteRequest : function(parentCoreEntity, attrName, adapterName, extraInfo, handlers, request){
			var attrNode = node_getEntityTreeNodeInterface(parentCoreEntity).getChild(attrName);
			var adapter = attrNode.getAdapters()[adapterName!=undefined?adapterName:node_COMMONCONSTANT.NAME_DEFAULT];
			if(adapter!=undefined)	return this.getAdapterExecuteRequest(parentCoreEntity, attrNode.getChildValue(), adapter, extraInfo, handlers, request);
		},
		
		getAdapterExecuteRequest : function(parentCoreEntity, childRuntime, adapter, extraInfo, handlers, request){
			var childInput;
			var childCore = childRuntime.getCoreEntity==undefined?undefined:childRuntime.getCoreEntity();
			if(childCore==undefined)   childInput = childRuntime;
			else{
				var childCoreType = node_getObjectType(childCore);
				if(childCoreType==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
					childInput = childCore.getMainEntity();
				}
				else{
					childInput = childRuntime;
				}
			}
			
			return adapter.getExecuteRequest(parentCoreEntity, childInput, extraInfo, handlers, request);
		},
	
		getRootConfigureRequest : function(configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	
			var getConfigureValueRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request, configureObject){
					
					var configureValue = configureObject==undefined?undefined:configureObject[node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_ROOT_VALUE)];
					var configureGlobal;
					var configureParms;
					
					if(configureValue!=undefined){
						configureGlobal = configureObject[node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_ROOT_GLOBAL)];
						configureParms = configureObject[node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_ROOT_PARM)];
					}
					else{
						configureValue = configureObject;
					}
					
					return node_createConfigure(configureValue, configureGlobal, configureParms);
				}
			});
			if(typeof configure === 'object'){
				getConfigureValueRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					return configure;
				}));
			}
			else if(typeof configure === 'string'){
				var configureName = configure;
				var settingName;
				var index = configure.indexOf("-");
				if(index!=-1){
					configureName = configure.substring(0, index);
					settingName = configure.substring(index+1);
				}
				
				var configureResourceId = new node_ResourceId(configureName, node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONFIGURE, "1.0.0");
				getConfigureValueRequest.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(configureResourceId, {
					success : function(requestInfo, resourceTree){
						var configureValue = node_resourceUtility.getResourceFromTree(resourceTree, configureResourceId).resourceData[node_COMMONATRIBUTECONSTANT.RESOURCEDATACONFIGURE_CONFIGURE];
						if(settingName!=undefined)   configureValue = configureValue[settingName];
						return configureValue;
					}
				}));
			}
			out.addRequest(getConfigureValueRequest);
			return out;
		},
		
	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("complexEntityUtility", node_complexEntityUtility); 

})(packageObj);
