{
	"info": {
		"name" : "value_none_test.complex.script" 
	},
	"entity":{
		"scriptName": "complexscript_test_value",
		"parm" : {
			"variable1" : ["reference_local", "normal", "override_parent"],
			"variable" : ["reference_local", "normal"]
		},
		"valueContext" :{
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"normal": {
							"status": "disabled1",
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						},
						"override_parent": {
							"status": "disabled",
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "123456666"
							}
						},
						"reference_local": {
							"status": "disabled1",
							"definition": {
								"reference" : {
									"parentValueContext": "self",
									"elementPath": "normal"
								}
							}
						}
					}
				}
			]
		} 			
	}
}
