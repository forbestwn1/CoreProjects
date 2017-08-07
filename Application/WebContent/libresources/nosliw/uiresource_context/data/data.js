/**
 * 
 */
function createDataWraper(obj){
	var objValueWraper = {
		'dataType':
		{
			'categary' : CONTEXT_TYPE_DATA,
		},
		'data' : obj,
	};
	
	_.extend(objValueWraper, Backbone.Events);
	return objValueWraper;
}

function getDataCategary(data){
	return data.dataType.categary;
}

function getDataType(data){
	return data.dataType.type;
}

function getDataAttributeByPath(obj, prop){
	if(prop==undefined || prop=='')  return obj;
	
    var parts = prop.split('.'),
        last = parts.pop(),
        l = parts.length,
        i = 1,
        current = parts[0];

    if(current==undefined)  return obj.data[last];
    
    while((obj = obj.data[current]) && i < l) {
        current = parts[i];
        i++;
    }

    if(obj) {
        return obj.data[last];
    }
}

