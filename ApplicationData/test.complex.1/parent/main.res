{
	"info": {
		"name": "parent complex entity",
		"description": "parent complex entity"
	},
	"entity": [
		{
			"info": {
				"name" : "value_none_test.complex.script", 
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_value",
				"parm" : {
					"variable" : ["normal", ""]
				},
				"valueStructure" :{
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
								},
								"relative_reference": {
									"definition": {
										"reference" : "parent_public"
									}
								},
								"relative_link": {
									"definition": {
										"link" : "parent_public",
										"definition": {
											"criteria":"test.string;1.0.0"
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
				"name" : "simplized1_none_test.complex.script", 
				"status": "disabled1"
			},
			"scriptName": "complexscript_test_exist"
		},
		{
			"info": {
				"name": "testlifecycle.log_none_test.complex.script",
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_lifecycle_log",
				"parm" : {
				}
			}
		},
		{
			"info": {
				"name" : "testparm_none_test.complex.script",
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_printparms",
				"parm" : {
					"name" : "simplized1_none_test.complex.script"
				}
			}
		},
		{
			"info": {
				"name" : "testconfigure_none_test.complex.script",
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_printconfigure",
				"parm" : {
					"name" : "simplized1_none_test.complex.script"
				}
			}
		},
		{
			"info": {
				"name" : "teststate_none_test.complex.script",
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_state",
				"parm" : {
					"state" : "state1"
				}
			}
		},
		{
			"info": {
				"name": "testlifecycle.rollback_none_test.complex.script",
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_lifecycle_rollback",
				"parm" : {
				}
			}
		},
		{
			"info": {
				"name": "normal1_set_test.complex.script",
				"status": "disabled"
			},
			"element": [
				{
					"info": {
						"name": "element1",
						"description": "element1"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_set_test.complex.script_element1",
							"state" : "element_set_1"
						}
					}
				},
				{
					"info": {
						"name": "element2",
						"description": "element2"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_set_test.complex.script_element2",
							"state" : "element_set_2"
						}
					}
				}
			]
		},
		{
			"info": {
				"name": "normal1_list_test.complex.script",
				"status": "disabled"
			},
			"element": [
				{
					"info": {
						"name": "element1",
						"description": "element1"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_list_test.complex.script_element1",
							"state" : "element_list_1"
						}
					}
				},
				{
					"info": {
						"name": "element2",
						"description": "element2"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_list_test.complex.script_element2",
							"state" : "element_list_2"
						}
					}
				}
			]
		},
		{
			"info": {
				"name": "attachment",
				"status": "disabled",
				"description": "parent attachment" 
			},
			"entity": {
				"valuestructure" : [
					{
						"name" : "valuestructure1",
						"entity": {
							"fromAttachment1": {
								"definition": {
									"criteria": "test.string;1.0.0"
								},
								"defaultValue": {
									"dataTypeId": "test.string;1.0.0",
									"value": "Default value for forlistservice_1_ex_parm1"
								}
							},
							"fromAttachment2": {
								"definition": {
									"criteria": "test.float;1.0.0"
								}
							}
						}
					}
				],
				"testsimple1": [
					{
						"name": "samename",
						"entity": {
							"parent_none_testsimple1": {
								"entity":{
								}
							}
						}
					},
					{
						"name": "inparentonly",
						"entity": {
							"parent_none_testsimple1": {
								"entity":{
								}
							}
						}
					}
				],
				"test.complex.1": [
				
				]
			}
		},
		{
			"info": {
				"name": "valueStructure",
				"status": "disabled"
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
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType" : "internal",
					"valueStructure" : {
						"parent_internal": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
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
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType": "public",
					"groupName" : "fromResource",
					"valueStructure" : {
						"resourceId" : "valuestructure|*test1"
					}
				},
				{
					"groupType": "public",
					"groupName" : "fromAttachment",
					"valueStructure" : {
						"reference": "valuestructure1;valuestructure"
					}
				}
			]
		}
	]
}
