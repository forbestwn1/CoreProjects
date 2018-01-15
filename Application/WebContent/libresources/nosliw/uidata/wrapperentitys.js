//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node

//*******************************************   Start Node Definition  ************************************** 	

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
			var path = loc_pathById[id];
			if(path==undefined){
				path = this.getIndexById(id)+"";
			}
			return path;
		},
		
		getIndexById : function(id){
			return loc_ids.indexOf(id);
//			var out = -1;
//			for(var i in loc_ids){
//				if(loc_ids[i]==id){
//					out = i;
//					break;
//				}
//			}
//			return out;
		},
		
		getIdByPath : function(path){
			var out;
			_.each(loc_pathById, function(p, id){
				if(path==p){
					out = p;
				}
			});
			if(out==undefined)  out = loc_ids[path];
			return out;
		},
		
		insertValue : function(value, index, path, id){
			if(id==undefined)  id = loc_generateId();
			loc_valueById[id] = value;
			if(path!=undefined)  loc_pathById[id] = path;
			loc_ids.splice(index, 0, id);
			return id;
		},
		
		deleteValue : function(index){
			var id = loc_ids[index];
			var path = this.getPathById();
			
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
