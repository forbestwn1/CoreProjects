/**
 * 
 */
nosliwObjectUtility = function(){
	
	return {
		createContextElementObject : function(data, path){
			var objValueWraper = {
					'dataType':
					{
						'categary' : CONTEXT_TYPE_OBJECT,
					},
					'data' : obj,
					'path' : path,
				};
				
				_.extend(objValueWraper, Backbone.Events);
				if(objValueWraper.data!=undefined){
					_.extend(objValueWraper.data, Backbone.Events);
				}
				return objValueWraper;
		},
	
		isAtomData : function(data){
			if(_.isString(data) || _.isNumber(data) || _.isBoolean(data))  return true;
		},

		getObjectContextEleData : function(contextEle){
			var data = contextEle.data.data;
			var path;
			if(contextEle.path!=undefined){
				path = contextEle.path+'';
			}
			return getObjectAttributeByPath(data, path);
		},

		getObjectAttributeByPath : function(obj, prop) {
			if(prop==undefined || prop=='')  return obj;
			
		    var parts = prop.split('.'),
		        last = parts.pop(),
		        l = parts.length,
		        i = 1,
		        current = parts[0];

		    if(current==undefined)  return obj[last];
		    
		    while((obj = obj[current]) && i < l) {
		        current = parts[i];
		        i++;
		    }

		    if(obj) {
		        return obj[last];
		    }
		},

		setProperty : function(obj, prop, value) {
			if(isStringEmpty(prop)){
				obj=value;
				return;
			}
			if(prop.indexOf('.')==-1){
				obj[prop]=value;
				return;
			}
			
		    var parts = prop.split('.'),
		        last = parts.pop(),
		        l = parts.length,
		        i = 1,
		        current = parts[0];

		    while((obj = obj[current]) && i < l) {
		        current = parts[i];
		        i++;
		    }

		    if(obj) {
		        obj[last]=value;
		    }
		}
	};	
}();
