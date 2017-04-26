/**
 * 
 */
packageName.OrderedContainer = function(dataWraperArray, childDataType, keyName){
	this.keyArray = [];
	this.dataMap = {};
	this.childDataType = childDataType;
	this.keyName = keyName;
	
	var dataArray = dataWraperArray;
	
	if(this.keyName==undefined){
		if(this.childDataType.categary=="entity" || this.childDataType.categary=="query"){
			this.keyName = "ID";
		}
	}

	for(var index in dataArray){
		this.addData(dataArray[index]);
	}
};

NosliwOrderedContainer.prototype = {
		addData : function(data){
			if(this.keyName==undefined){
				var index=0;
				if(this.dataArray.length>0){
					index = this.dataArray[this.dataArray.length-1] + 1;
				}
				this.keyArray.push(index);
				this.dataMap[index] = data;
			}
			else{
					var keyValue = getObjectAttributeByPath(data, this.keyName);
					this.keyArray.push(keyValue);
					this.dataMap[keyValue] = data;
			}
		},
		
		removeData : function(key){
			for(var i in this.keyArray){
				if(this.keyArray[i]==key){
					this.keyArray.splice(i, 1);
					break;
				}
			}
			delete this.dataMap[key];
		},
		
		updateData : function(data, key){
			if(key!=undefined){
			}
			else{
				if(this.keyName!=undefined){
					key = getObjectAttributeByPath(data, this.keyName);
				}
			}
			
			if(key!=undefined){
				this.dataMap[key].data = data.data;
				return this.dataMap[key];
			}
		},
		
		getKeyArray : function(){
			return this.keyArray;
		},
		
		getDataByKey : function(key){
			return this.dataMap[key];
		},
		
		getDataList : function(){
			var out = [];
			for(var key in this.getKeyArray()){
				out.push(this.getDataByKey(key));
			}
		},
};


function handleDataContainerEachElement(dataContainerWraper, handler){
	if(dataContainerWraper==undefined)   return;
	var dataContainer = dataContainerWraper.container;
	var childDataType = dataContainerWraper.childDataType;
	var keyArray = dataContainer.getKeyArray();
	for(var key in keyArray){
		var eleWraper = dataContainer.getDataByKey(key);
		var contextEle = createContextElement(eleWraper);
    	handler(key, eleWraper, contextEle);
	}
	
}

