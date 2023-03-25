{
	"info": {
		"name": "test value",
		"description": "parent complex entity"
	},
	"entity": [
		{
			"info": {
				"name" : "localresource.reference_none_test.complex.script",
				"status": "disabled"
			},
			"resourceId": "test.complex.script|#reference"
		},
		{
			"info": {
				"name" : "reference_none_test.complex.script", 
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_value",
				"parm" : {
					"variable1" : ["reference_link_1", "reference_definition_1"],
					"variable" : ["reference_link_1", "reference_definition_1"]
				},
				"valueContext" :{
					"entity": [
						{
							"groupType" : "public",
							"valueStructure" : {
								"reference_link_1": {
									"status": "disabled1",
									"definition": {
										"definition" : {
											"elementPath": "parent_public"
										}
									}
								},
								"reference_definition_1": {
									"status": "disabled1",
									"definition": {
										"link" : {
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
				"name" : "merge.runtime_none_test.complex.script", 
				"status": "disabled"
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
					"variable" : ["parent_public", "parent_protected", "parent_private", "parent_internal"]
				}
			}
		},
		{
			"info": {
				"name" : "merge.definition_none_test.complex.script", 
				"status": "disabled"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "definition"
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
				"name" : "merge.none_none_test.complex.script", 
				"status": "disabled"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "none"
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
								"value": "default value of parent_public"
							}
						}
					}
				},
				{
					"groupType" : "protected",
					"valueStructure" : {
						"parent_protected": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "default value of parent_protected"
							}
						}
					}
				},
				{
					"groupType" : "private",
					"valueStructure" : {
						"parent_private": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "default value of parent_private"
							}
						}
					}
				},
				{
					"groupType" : "internal",
					"valueStructure" : {
						"parent_public": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "default value of parent_internal"
							}
						}
					}
				}
			]
		}
	]
}
