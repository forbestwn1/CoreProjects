//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.geo");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define operation
//define operation
dataTypeDefition.operations['distance'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			var fromLat = parms.getParm("from").value.latitude;
			var fromLon = parms.getParm("from").value.longitude;
			var toLat = parms.getParm("to").value.latitude;
			var toLon = parms.getParm("to").value.longitude;

		    var prv_calcCrow = function(lat1, lon1, lat2, lon2) 
		    {
		      var R = 6371; // km
		      var dLat = prv_toRad(lat2-lat1);
		      var dLon = prv_toRad(lon2-lon1);
		      var lat1 = prv_toRad(lat1);
		      var lat2 = prv_toRad(lat2);

		      var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		      var d = R * c;
		      return d;
		    };

		    var prv_toRad = function(Value) 
		    {
		        return Value * Math.PI / 180;
		    };

		    var distance = prv_calcCrow(fromLat, fromLon, toLat, toLon);
			
			return {
				dataTypeId : "test.distance;1.0.0",
				value : {
					distance : distance,
					unit : "km"
				}
			}
		},

		//This function takes in latitude and longitude of two location and returns the distance between them as the crow flies (in km)
	    prv_calcCrow : function(lat1, lon1, lat2, lon2) 
	    {
	      var R = 6371; // km
	      var dLat = this.prv_toRad(lat2-lat1);
	      var dLon = this.prv_toRad(lon2-lon1);
	      var lat1 = this.prv_toRad(lat1);
	      var lat2 = this.prv_toRad(lat2);

	      var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
	      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	      var d = R * c;
	      return d;
	    },

	    // Converts numeric degrees to radians
	    prv_toRad : function(Value) 
	    {
	        return Value * Math.PI / 180;
	    },
		
		requires:{
		},
};


nosliw.addDataTypeDefinition(dataTypeDefition);
