/*
 * elementInfosArray : an array of element info describing context element
 * 
 */
var nosliwCreateContext = function(elementInfosArray, request){
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(requestInfo){
		_.each(loc_out.prv_elements, function(element, name){
			//clear up variable
			element.variable.destroy(requestInfo);
		});
		loc_out.prv_elements = {};
		
		loc_out.prv_eventSource.clearup();
	};
	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(elementInfosArray, request){
		//context elements (wrapper variables)
		loc_out.prv_elements = {};
		//object used as event source
		loc_out.prv_eventSource = nosliwCreateRequestEventSource();
		
		_.each(elementInfosArray, function(elementInfo, key){
			//create empty wrapper variable for each element
			loc_out.prv_elements[elementInfo.name] = nosliwCreateContextElement(elementInfo, request);
		});
	};
	
	var loc_out = {
		
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
		
		/*
		 * get all context elements
		 */
		getContext : function(){ return this.prv_elements; },
		
		/*
		 * get context element by name
		 */
		getContextElement : function(name){ return this.getContext()[name]; },

		/*
		 * get context element variable by name
		 */
		getContextElementVariable : function(name){ 
			var contextEle = this.getContextElement(name);
			if(contextEle==undefined)  return undefined;
			return contextEle.variable;
		},

		/*
		 * get data of context element
		 */
		getContextElementData : function(name){ 
			var contextEle = this.getContextElement(name);
			if(contextEle==undefined)  return undefined;
			return contextEle.variable.getData();
		},
		
		/*
		 * create context variable
		 */
		createVariable : function(contextVariable, requestInfo){
			var parentVar = this.getContextElementVariable(contextVariable.name);
			var variable = nosliwCreateWrapperVariable(parentVar, contextVariable.path, requestInfo);
			return variable;
		},
		
		/*
		 * update context wrappers
		 * elements: 
		 * 		name --- wrapper
		 * 		new wrappers
		 * only update those element variables contains within wrappers 
		 */
		updateContext : function(wrappers, requestInfo){
			this.prv_eventSource.triggerEvent(NOSLIWCONSTANT.CONTEXT_EVENT_BEFOREUPDATE, this, requestInfo);

			var that = this;
			_.each(wrappers, function(wrapper, name){
				//set wrapper to each variable
				var eleVar = that.getContextElementVariable(name);
				if(eleVar!=undefined){
					eleVar.setWrapper(wrapper, requestInfo);
				}
			});

			this.prv_eventSource.triggerEvent(NOSLIWCONSTANT.CONTEXT_EVENT_UPDATE, this, requestInfo);
			
			this.prv_eventSource.triggerEvent(NOSLIWCONSTANT.CONTEXT_EVENT_AFTERUPDATE, this, requestInfo);
		},
		
		/*
		 * register context event listener
		 * return : listener object
		 */
		registerContextEvent : function(handler, thisContext){
			return this.prv_eventSource.registerEventHandler(handler, thisContext);
		},
	};

	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out);
	loc_out = nosliwTypedObjectUtility.makeTypedObject(loc_out, NOSLIWCONSTANT.TYPEDOBJECT_TYPE_CONTEXT);
	loc_out.init(elementInfosArray, request);
	return loc_out;
};

/*
 * object to describe context element info, two models:
 * 		1. name + parent context + parent context contextVariable
 * 		2. name + info + wrapper
 */
nosliwCreateContextElementInfo = function(name, data1, data2){
	var loc_out = {
		name : name,
	};
	var type = nosliwTypedObjectUtility.getObjectType(data1);
	if(type==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_CONTEXT){
		//input is context + context variable
		loc_out.context = data1;
		loc_out.contextVariable = nosliwCreateContextVariable(data2);
	}
	else{
		//
		loc_out.info = data1;
		loc_out.wrapper = data2;
	}
	return loc_out;
};

/*
 * create real context element based on element info 
 * it contains following attribute:
 * 		name
 * 		variable
 * 		info
 */
nosliwCreateContextElement = function(elementInfo, requestInfo){
	var loc_out = {
		name : elementInfo.name,
		info : elementInfo.info,
	};

	if(elementInfo.context!=undefined){
		var context = elementInfo.context;
		var contextVar = elementInfo.contextVariable;
		loc_out.variable = context.createVariable(contextVar, requestInfo);
	}
	else{
		var wrapper = elementInfo.wrapper;
		loc_out.variable = nosliwCreateWrapperVariable(wrapper, requestInfo);
	}

	return loc_out;
};
