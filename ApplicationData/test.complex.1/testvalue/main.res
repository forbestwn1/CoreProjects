{
	"info": {
		"name": "test value",
		"description": "parent complex entity"
	},
	"entity": [
		{
			"info": {
				"name" : "normal.none_none_test.complex.script", 
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
				}
			}
		},
		{
			"embeded": {
				"info": {
					"name" : "globalresource.reference_none_test.complex.script",
					"status": "disabled1"
				},
				"resourceId": "test.complex.script|*value"
			},
			"adapter": {
				"entityType" : "dataAssociation",
				"entity" : {
					"mapping" : {
						"aaa_module" : {
							"definition" : {
								"path" : "simpleOutput2"
							}
						}
					}
				}
			}
		},
		{
			"info": {
				"name" : "localresource.reference_none_test.complex.script",
				"status": "disabled"
			},
			"resourceId": "test.complex.script|#reference"
		},
		{
			"info": {
				"name" : "localresource.merge.runtime_none_test.complex.script",
				"status": "disabled"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "runtime"
					}
				}
			},			
			"resourceId": "test.complex.script|#merge"
		},
		{
			"info": {
				"name" : "localresource.merge.definition_none_test.complex.script", 
				"status": "disabled"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "definition"
					}
				}
			},
			"resourceId": "test.complex.script|#merge"
		},
		{
			"info": {
				"name" : "localresource.merge.none_none_test.complex.script", 
				"status": "disabled"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "none"
					}
				}
			},
			"resourceId": "test.complex.script|#merge"
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
						},
						"parent_public_withchildren": {
							"definition": {
								"child" : {
									"a" : {
										"child" : {
											"aa" : {"criteria":"test.string;1.0.0"}
										}
									}
								}
							},
							"defaultValue": {
								"a": {
									"aa" : {
										"dataTypeId": "test.string",
										"value": "default value of a.aa"
									}
								}
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
