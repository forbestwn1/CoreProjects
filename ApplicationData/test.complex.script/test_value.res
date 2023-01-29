{
	"info": {
		"name" : "value_none_test.complex.script", 
		"status": "disabled1"
	},
	"entity":{
		"scriptName": "complexscript_test_value",
		"parm" : {
			"variable" : ["normal"]
		},
		"valueContext" :{
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"normal": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						},
						"override_parent": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				}
			]
		} 			
	}
},
