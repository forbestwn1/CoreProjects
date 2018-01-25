//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node

//*******************************************   Start Node Definition  ************************************** 	

	
	/**
	 * Container for ordered elements in wrapper
	 * value : value of element
	 * index : position in array, index for element may change
	 * id	 : id for element that wont change; id is also internal path
	 * 		id use path if path exist
	 * 		otherwise, generate id 
	 * path  : path related with container. 
	 * 		if path exists, it is used as id. 
	 * 		if path not exists, use index as path.
	 */
var node_createWrapperOrderedContainer = function(){
	
	//id generation 
	var loc_id = 0;
	
	var loc_valueById = {};
	var loc_pathById = {};
	var loc_ids = [];

	var loc_generateId = function(){
		loc_id++;
		return "id"+loc_id+"";
	};
	
	var loc_getIdByIndex = function(index){
		return loc_ids[index];
	};
	
	var loc_out = {
		getValueByIndex : function(index){		return this.getValueById(loc_getIdByIndex(index));		},
		
		getValueById : function(id){	return loc_valueById[id];	},
		
		getPathById : function(id){
			var path = loc_pathById[id];       //find from provided
			if(path==undefined)		path = this.getIndexById(id)+"";   //not provided, then use index as path
			return path;
		},
		
		getIndexById : function(id){	return loc_ids.indexOf(id);	},
		
		getIdByPath : function(path){
			var out;
			_.each(loc_pathById, function(p, id){
				if(path==p){
					out = p;
				}
			});
			if(out==undefined)  out = loc_ids[path];    //if not provided, treat path as index
			return out;
		},
		
		insertValue : function(value, index, path){
			var id = path;
			if(id==undefined)  id = loc_generateId();
			
			loc_valueById[id] = value;
			loc_ids.splice(index, 0, id);
			
			if(path!=undefined)  loc_pathById[id] = path;
			return id;
		},
		
		deleteElementByIndex : function(index){
			var id = loc_ids[index];
			var path = this.getPathById(id);
			
			loc_ids.splice(index, 1);
			delete loc_valueById[id];
			delete loc_pathById[id];
			
			return path;
		},

		getAllValue : function(){
			var out = [];
			for(var i in loc_ids){
				out.push(this.getValueByIndex(i));
			}
			return out;
		},
		
		clear : function(){
			loc_id = 0;
			loc_valueById = {};
			loc_pathById = {};
			loc_ids = [];
		}
		
	};
	return loc_out;
};	
	


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("createWrapperOrderedContainer", node_createWrapperOrderedContainer); 

})(packageObj);
