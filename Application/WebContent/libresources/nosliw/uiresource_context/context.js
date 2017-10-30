/**
 * 
 */

var nosliwCreateContextElement = function(wraper, path){
	var dataType = wraper.dataType;
	
	var contextEle = undefined;
	switch(dataType.categary){
	case NOSLIWCOMMONCONSTANT.DATATYPE_CATEGARY_OBJECT:
		contextEle = nosliwCreateContextElementObject(wraper, path);
		break;
	case NOSLIWCOMMONCONSTANT.DATATYPE_CATEGARY_ENTITY:
		contextEle = nosliwCreateContextElementEntity(wraper, path);
		break;
	}
	
	_.extends(nosliwCreateContextElementBasic(wraper, path), contextEle);
	
	//append resource and object life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out);
};	
	
var nosliwCreateContextElementBasic = function(wraper, path){
	var loc_wraper = wraper;
	var loc_path = path;
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){
		return loc_out.ovr_destroy.apply(this, arguments); 
	};
	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){
		return loc_out.ovr_init.apply(this, arguments); 
	};

	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){
		return loc_out.ovr_deactive();
	};
	
	
	var loc_out = {
			
			ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

			ovr_getType : function(){},
			
			ovr_init : function(){},
			ovr_destroy : function(){},
			
			ovr_registerEvent : function(path, handler, thisContext){},
			ovr_unregisterEvent : function(eventObj){},
			ovr_getWraperByPath : function(path){},
			ovr_request : function(reqInfo){},
			ovr_handleEachElement : function(handler, thatContext){},
			
			getType : function(){return this.ovr_getType();},
			getWraper : function(){return loc_wraper;},
			getPath : function(){return loc_path;},
			setWraper : function(){loc_wraper = wraper;},
			setPath : function(path){loc_path = path;},
			
			registerEvent : function(path, handler, thisContext){
				
			},
			
			unregisterEvent : function(eventObj){
				
			},
	
			getWraperByPath : function(path){
			
			},
			
			request : function(reqInfo){
				
			},

			handleEachElement : function(handler, thatContext){
			
			},
	};
	
	return loc_out;
};


var NosliwUIContextUtility = function(){

	return {
		/*
		 * create event object for context variable 
		 * handler : callback method with parms, when this method is called, some event related with context variable happened, and this event is original from request info
		 * 		event				event name
		 * 		contextVariable		context variable related to the event
		 * 		eventData			data related with this event
		 * 		requestInfo			request info
		 */
		registerContextVariableEvent: function(context, contextVariable, handler, thisContext){
			var contextEle = context[contextVariable.name];
			if(contextEle==undefined)  return;
			var type = contextEle.type;
			if(type==NOSLIWCONSTANT.CONTEXT_TYPE_ENTITY){
				return registerEntityContextEvent(context, contextVariable, handler, thisContext);
			}
			else if(type==NOSLIWCONSTANT.CONTEXT_TYPE_QUERYENTITY){
				return registerQueryContextEvent(context, contextVariable, handler, thisContext);
			}
			else if(type==NOSLIWCONSTANT.CONTEXT_TYPE_CONTAINER){
				return registerContainerContextEvent(context, contextVariable, handler, thisContext);
			}
			else if(type==NOSLIWCONSTANT.CONTEXT_TYPE_OBJECT){
				return registerObjectContextEvent(context, contextVariable, handler, thisContext);
			}
			else if(type==NOSLIWCONSTANT.CONTEXT_TYPE_DATA){
				
			}
			else if(type==NOSLIWCONSTANT.CONTEXT_TYPE_SERVICE){
				return registerServiceContextEvent(context, contextVariable, handler, thisContext);
			}
		},
	
		/*
		 * create event object for context variable 
		 */
		unregisterContextVariableEvent : function(contextVariableEventObj){
			var type = contextVariableEventObj.contextType;
			if(type==CONTEXT_TYPE_ENTITY){
				unregisterEntityContextEvent(contextVariableEventObj);
			}
			else if(type==CONTEXT_TYPE_QUERYENTITY){
				
			}
			else if(type==CONTEXT_TYPE_CONTAINER){
				unregisterContainerContextEvent(contextVariableEventObj);
			}
			else if(type==CONTEXT_TYPE_OBJECT){
				unregisterObjectContextEvent(contextVariableEventObj);
			}
			else if(type==CONTEXT_TYPE_DATA){

			}
		},
		
		createContextElement : function(data, path){
			var contextEle;
			var dataType = data.dataType;
			if(dataType.categary==CONTEXT_TYPE_ENTITY){
				contextEle = createContextElementEntity(data, path);
			}
			else if(dataType.categary==CONTEXT_TYPE_QUERYENTITY){
				contextEle = createContextElementQueryEntity(data);
			}
			else if(dataType.categary==CONTEXT_TYPE_CONTAINER){
				contextEle = createContextElementContainer(data, path);
			}
			else if(dataType.categary==CONTEXT_TYPE_OBJECT){
				contextEle = createContextElementObject(data, path);
			}
			else if(dataType.categary=="simple" || dataType.categary=="body"){   //?????
				contextEle = createContextElementData(data, path);
			}
			else if(dataType.categary==CONTEXT_TYPE_DATA){
				contextEle = createContextElementData(data, path);
			}
			else if(dataType.categary==CONTEXT_TYPE_SERVICE){
				contextEle = createContextElementService(data);
			}
			return contextEle;
		},
		
		getContextPathWraper : function(context, path){
			if(context==undefined)   return;
			
			var contextPath = getPathInfo(path);
			
			var name = contextPath.name;
			var contextEle = context[name];
			if(contextEle==undefined)   return undefined;
			
			var path = getAbsolutePathOfContextData(context, contextPath);
			var type = contextEle.type;
			if(type==CONTEXT_TYPE_ENTITY){
				return getEntityAttributeWraperByPath(contextEle.data, path);
			}
			else if(type==CONTEXT_TYPE_QUERYENTITY){
				return getQueryEntityAttributeWraperByPath(contextEle.data, path);
			}
			else if(type==CONTEXT_TYPE_CONTAINER){
				return contextEle;
			}
			else if(type==CONTEXT_TYPE_OBJECT){
				return createObjectWraper(getObjectAttributeByPath(contextEle.data.data, path));
			}
			else if(type==CONTEXT_TYPE_DATA){
				return createDataWraper(getDataAttributeByPath(contextEle.data.data, path));
			}
			else if(type==CONTEXT_TYPE_SERVICE){
				return getServiceDataWraperByPath(context, contextPath);
			}
		},


		requestOperateContextPathValue : function(reqInfo){
			var serviceData;
			
			var reqOperations = reqInfo.service.data;
			var reqOperation = reqOperations[0];
			var context = reqOperation.context;
			var contextPath = getPathInfo(reqOperation.contextPath);
			var data = reqOperation.data;
			var contextEle = context[contextPath.name];
			var type = contextEle.type;
			
			if(type==CONTEXT_TYPE_ENTITY){
				serviceData = NosliwEntityOperationRequestManager.requestOperateEntityContextPathValue(reqInfo);
			}
			else if(type==CONTEXT_TYPE_CONTAINER){
			}
			else if(type==CONTEXT_TYPE_OBJECT){
				serviceData = requestSetObjectContextPathValue(context, contextPath, data, reqInfo);
			}
			else if(type==CONTEXT_TYPE_SERVICE){
				serviceData = requestOperateServiceContextPathValue(reqInfo);
			}
			return serviceData;
		},

		requestSetContextPathValue : function(context, contextPath, value, reqInfo){
			var serviceData;
			var name = contextPath.name;
			var contextEle = context[name];
			var type = contextEle.type;
			if(type==CONTEXT_TYPE_ENTITY){
				serviceData = requestSetEntityContextPathValue(context, contextPath, value, reqInfo);
			}
			else if(type==CONTEXT_TYPE_CONTAINER){
			}
			else if(type==CONTEXT_TYPE_OBJECT){
				serviceData = requestSetObjectContextPathValue(context, contextPath, value, reqInfo);
			}
			return serviceData;
		},

		createSubContextElement : function(context, subPath, clone){
			
			var subPathInfo = getPathInfo(subPath);
		/*
			if(_.isString(subPath)){
				subPathInfo = {};
				var k = subPath.indexOf(".");
				if(k==-1){
					subPathInfo.name = subPath;
				}
				else{
					subPathInfo.name = subPath.substring(0, k);
					subPathInfo.path = subPath.substring(k+1);
				}
			}
			else{
				subPathInfo = subPath;
			}
		*/	
			var name = subPathInfo.name;
			var contextEle = context[name];
			if(contextEle==undefined)   return undefined;
			
			var path = getAbsolutePathOfContextData(context, subPathInfo);
			var type = contextEle.type;
			
			var out;
			if(type==CONTEXT_TYPE_ENTITY){
				out = createContextElementEntity(contextEle.data, path);
			}
			else if(type==CONTEXT_TYPE_QUERYENTITY){
			}
			else if(type==CONTEXT_TYPE_CONTAINER){
			}
			else if(type==CONTEXT_TYPE_OBJECT){
				if(clone==true){
					var subObject = getObjectAttributeByPath(contextEle.data.data, path);
					var subObject1 = cloneObject(subObject);
					out = createContextElementObject(createObjectWraper(subObject1));
				}
				else{
					out = createContextElementObject(contextEle.data, path);
				}
			}
			else if(type==CONTEXT_TYPE_DATA){
			}
			return out;
		},

		handleContextContainerEachElement : function(context, contextPath, handler, that){
			var contextValue = context[contextPath.name];
			var type = contextValue.type;
			if(type==CONTEXT_TYPE_ENTITY){
				var containerPath = cascadePath(contextValue.path, contextPath.path);
				var containerWraper = getEntityAttributeWraperByPath(contextValue.data, containerPath);
				var containerData = containerWraper.data;
				for (var key in containerData) {
				    if (containerData.hasOwnProperty(key)) {
				    	var eleWraper = containerData[key];
				    	
						var categary = getWraperDataCategary(eleWraper);
						if(categary=='reference'){
							eleWraper =  NosliwEntityManager.getEntity(eleWraper.data);
					    	var contextEle = createContextElement(eleWraper); 
					    	handler.call(that, key, eleWraper, contextEle);
						}
						else{
					    	var contextEle = createContextElement(contextValue.data, cascadePath(containerPath, key)); 
					    	handler.call(that, key, eleWraper, contextEle);
						}
				    }
				}	
			}
			else if(type==CONTEXT_TYPE_CONTAINER){
				var containerWraper = contextValue.data;
				var container = containerWraper.container;
				var keyArray = container.getKeyArray();
				for (var index in keyArray) {
					var key = keyArray[index];
					var eleWraper = container.getDataByKey(key);
			    	var contextEle = createContextElement(eleWraper); 
			    	handler.call(that, key, eleWraper, contextEle);
				}	
			}
			else if(type==CONTEXT_TYPE_DATA){
				var containerData = contextValue.data.data;
				var categary = getDataCategary(containerData);
				if(categary=='container'){
					_.each(containerData.data, function(data, key, list){
						var contextEle = createContextElementData(createDataWraper(data));
				    	handler.call(that, key, data, contextEle);
					}, this);
				}
			}
			else if(type==CONTEXT_TYPE_OBJECT){
				var containerPath = cascadePath(contextValue.path, contextPath.path);
				var containerData = getObjectAttributeByPath(contextValue.data.data, containerPath);
				
				_.each(containerData, function(data, key, list){
					var contextEle = createContextElementObject(contextValue.data, cascadePath(containerPath, key));
			    	handler.call(that, key, data, contextEle);
				}, this);
			}
		},
	};
}();
