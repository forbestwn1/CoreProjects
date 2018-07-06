/**
 *         Context 
 */
nosliwEntityUtility = function(){
	
	return {
		cloneContext : function(context){
			contextNew = {};
			for (var name in context) {
			    if (context.hasOwnProperty(name)) {
					var value = context[name];
					contextNew[name] = value;
			    }
			}
			return contextNew;
		},

		mergeContext : function(){
			contextNew = {};
			for(var i in arguments){
				var context = arguments[i];
				for (var name in context) {
				    if (context.hasOwnProperty(name)) {
						var value = context[name];
						contextNew[name] = value;
				    }
				}
			}
			return contextNew;
		},

		getPathInfo : function(path){
			var out;
			if(_.isString(path)){
				out = {};
				var k = path.indexOf(".");
				if(k==-1){
					out.name = path;
				}
				else{
					out.name = path.substring(0, k);
					out.path = path.substring(k+1);
				}
			}
			else{
				out = path;
			}
			return out;
		},
		
		getAbsolutePathOfContextData : function(context, path){
			var dataPath = getPathInfo(path);
			
			return cascadePath(context[dataPath.name].path, dataPath.path);
		},

		getAbsoluteContextPath : function(context, dataPath){
			var out = {
				name : dataPath.name,
				path : cascadePath(context[dataPath.name].path, dataPath.path),
			};
			return out;
		},
		
		setContext : function(context, name, wraper, path){
			var contextEle = createContextElement(wraper, path);
			context[name] = contextEle;
		}

		
	};
};

function handleContextContainerElement(context, contextPath, key, handler){
	var contextValue = context[contextPath.name];
	var type = contextValue.type;
	if(type==CONTEXT_TYPE_ENTITY){
		var containerPath = cascadePath(contextValue.path, contextPath.path);
		var containerWraper = getEntityAttributeWraperByPath(contextValue.data, containerPath);
		var containerData = containerWraper.data;
    	var eleWraper = containerData[key];
    	var contextEle = createContextElement(contextValue.data, cascadePath(containerWraper, key)); 
    	handler(key, eleWraper, contextEle);
	}
}



function createContextElementQueryEntity(data){
	var ID = data.ID;
	var context = {};
	context.type = CONTEXT_TYPE_QUERYENTITY;
	context.data = data;
	context.ID = ID;
	return context;
}

function createContextElementEntity(data, path){
	var ID = data.ID;
	var context = {};
	context.type = CONTEXT_TYPE_ENTITY;
	context.data = data;
	context.path = path;
	context.ID = ID;
	return context;
}

function createContextElementData(data, path){
	context = {};
	context.type = CONTEXT_TYPE_DATA;
	context.data = data;
	context.path = path;
	return context;
}

function createContextElementObject(data, path){
	context = {};
	context.type = CONTEXT_TYPE_OBJECT;
	context.data = data;
	context.path = cascadePath(data.path, path);
	data.path = undefined;
	return context;
}

function createContextElementContainer(container, path){
	context = {};
	context.type = CONTEXT_TYPE_CONTAINER;
	context.data = container;
	return context;
}

function createContextElementService(data){
	context = {};
	context.type = CONTEXT_TYPE_SERVICE;
	context.data = data;
	return context;
}



var getExpressionContextValueWraper = function(context, name, path){
	return getContextPathWraper(context, {'name':name, 'path':path});
}

var getExpressionContextWraperValue = function(wraper){
	if(wraper!=undefined){
		var categary = wraper.dataType.categary;
		if(categary==CONTEXT_TYPE_DATA){
			return wraper.data.data;
		}
		else{
			return wraper.data;
		}
	}
}
