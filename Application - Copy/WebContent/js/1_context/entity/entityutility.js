/**
 * 
 */
nosliwEntityUtility = function(){
	
	return {
		isWraperAtomType : function(wraper){
			var categary = getWraperDataCategary(wraper);
			if(categary=='simple' || categary=='block'){
				return true;
			}
			return false;
		},

		setEntityRealPathValue : function(ID, path, value){
			var wraper = NosliwEntityManager.getEntity(ID);
			var pathParts = path.split(".");
			for(var i in pathParts){
				var part = pathParts[i];
				wraper = wraper.data[part];
			}
			wraper.data = value;
		},

		getRealEntity : function(ID, data){
			var idParts = ID.split(':');
			if(idParts.length<2)   return data;
			return getEntityAttributeWraperByPath(data, idParts[2]);
		},

		getRootEntityID : function(ID){
			var idParts = ID.split(':');
			return idParts[0]+':'+idParts[1];
		},

		getWraperAttributeValue : function(wraper, attr){
			var data = wraper.data;
			if(data==undefined)  return undefined;
			if(getWraperDataCategary(wraper)!='reference'){
				return data[attr];
			}
			else{
				var entityWraper = NosliwEntityManager.getEntity(data);
				if(entityWraper==undefined)  return undefined;
				return entityWraper.data[attr];
			}
		},

		getRealEntityAttribute : function(ID, attrPath, data){
			var contextPathInfo = {
					'ID' : ID,
					'data' : data,
					'path' : attrPath,
					'expectPath' : attrPath, 
			};
			loc_getRealAttribute(contextPathInfo);
			var results = contextPathInfo.results;
			return results.pop();
		},

		getEntityTypeFromID : function(ID){
			var i = ID.indexOf('#');
			i = i + 1;
			
			var j = ID.indexOf(":");
			if(j==-1)  j = ID.length;
			return ID.substring(i, j);
		},

		getDefinitionChild : function(def, child){
			var categary = def.dataCategary;
			if(categary==undefined)  return def.attributes[child];
			if(categary=='simple'||categary=='body'){
				return undefined;
			}
			else if(categary=='container'){
				var childCategary = def.childDataCategary;
				if(childCategary=='simple'||childCategary=='body'){
					return undefined;
				}
				else{
//					var childDataType = def.childDataType;
//					var attrEntityDef = NosliwEntityDefinitionManager.getEntityDefinitionByName(def.childDataType);
					if(def.first==true || def.first==undefined){
						def.first=false;
					}
					return def;
				}
			}
			else if(categary=='entity'){
				var attrEntityDef = NosliwEntityDefinitionManager.getEntityDefinitionByName(def.dataType);
				return attrEntityDef.attributes[child];
			}
		},


		
	};
	
}();


