//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createEventObject;
var node_eventUtility;
var node_createContextElement;
var node_createWrapperVariable;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	
/*
 * elementInfosArray : an array of element info describing context element
 * 
 */
var node_createContext = function(elementInfosArray, request){
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		_.each(loc_out.prv_elements, function(element, name){
			//clear up variable
			element.variable.destroy(requestInfo);
		});
		loc_out.prv_elements = {};
		
		loc_out.prv_eventObject.clearup();
	};
	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(elementInfosArray, request){
		//context elements (wrapper variables)
		loc_out.prv_elements = {};
		//object used as event object
		loc_out.prv_eventObject = node_createEventObject();
		
		_.each(elementInfosArray, function(elementInfo, key){
			//create empty wrapper variable for each element
			loc_out.prv_elements[elementInfo.name] = node_createContextElement(elementInfo, request);
		});
	};
	
	var loc_out = {
		
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
			var variable = node_createWrapperVariable(parentVar, contextVariable.path, requestInfo);
			//add extra attribute "contextPath" to variable for variables name under context
			variable.contextPath = node_namingConvensionUtility.cascadePath(contextVariable.name, contextVariable.path);
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
			this.prv_eventObject.triggerEvent(node_CONSTANT.CONTEXT_EVENT_BEFOREUPDATE, this, requestInfo);

			var that = this;
			_.each(wrappers, function(wrapper, name){
				//set wrapper to each variable
				var eleVar = that.getContextElementVariable(name);
				if(eleVar!=undefined){
					eleVar.setWrapper(wrapper, requestInfo);
				}
			});

			this.prv_eventObject.triggerEvent(node_CONSTANT.CONTEXT_EVENT_UPDATE, this, requestInfo);
			
			this.prv_eventObject.triggerEvent(node_CONSTANT.CONTEXT_EVENT_AFTERUPDATE, this, requestInfo);
		},
		
		/*
		 * register context event listener
		 * return : listener object
		 */
		registerContextListener : function(listener, handler, thisContext){
			node_eventUtility.registerListener(listener, this.prv_eventObject, node_CONSTANT.EVENT_EVENTNAME_ALL, handler, thisContext)
		},
	};

	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXT);
	
	node_getLifecycleInterface(loc_out).init(elementInfosArray, request);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElement", function(){node_createContextElement = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createWrapperVariable", function(){node_createWrapperVariable = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createContext", node_createContext); 

})(packageObj);
