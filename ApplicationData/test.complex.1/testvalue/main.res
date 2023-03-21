{
	"info": {
		"name": "test value",
		"description": "parent complex entity"
	},
	"entity": [
		{
			"info": {
				"name" : "value1_none_test.complex.script", 
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_value",
				"parm" : {
					"variable1" : ["reference_local1", "normal", "override_parent"],
					"variable" : ["reference_local1"]
				},
				"valueContext" :{
					"entity": [
						{
							"groupType" : "public",
							"valueStructure" : {
								"normal": {
									"status": "disabled",
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
								"reference_local1": {
									"status": "disabled1",
									"definition": {
										"link" : {
											"parentValueContext1": "self",
											"elementPath": "parent_public"
										}
									}
								}
							}
						}
					]
				} 			
			}
		},
		{
			"info": {
				"name" : "value2_none_test.complex.script", 
				"status": "disabled"
			},
			"entity":{
				"scriptName": "complexscript_test_value",
				"parm" : {
					"variable1" : ["reference_local2"],
					"variable" : ["reference_local2"]
				},
				"valueContext" :{
					"entity": [
						{
							"groupType" : "public",
							"valueStructure" : {
								"reference_local2": {
									"status": "disabled1",
									"definition": {
										"link" : {
											"parentValueContext1": "self",
											"elementPath": "parent_public"
										}
									}
								}
							}
						}
					]
				} 			
			}
		},
		{
			"info": {
				"name" : "value3_none_test.complex.script", 
				"status": "disabled1"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "runtime"
					}
				}
			},
			"entity":{
				"scriptName": "complexscript_test_value",
				"parm" : {
					"variable1" : ["parent_public"],
					"variable" : ["parent_public"]
				}
			}
		},
		{
			"info": {
				"name": "valueContext",
				"status": "disabled1"
			},
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"parent_public": {
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
	]
}
