{
	"extra": {
		"name": "parent complex entity",
		"description": "parent complex entity"
	},
	"entity": {
		"ignore_variable" : "parent_public",
		"simplized1_none_testsimple1": {
			"extra": {
				"status": "disabled1"
			},
			"scriptName": "testsimple1",
			"parm" : {
				"name" : "simplized1_none_testsimple1",
				"state" : "state1111"
			}
		},
		"entity1_none_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
				"scriptName": "testsimple1"
			}
		},
		"entity2_none_test.complex.1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
			}
		},
		"globalresource1_none_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"resourceId": "testsimple1|*test1"
		},
		"reference1_none_testsimple1": { 
			"extra": {
				"status": "disabled"
			},
			"reference": "samename"
		},
		"normal1_set_testsimple1": {
			"extra": {
				"status": "disabled1"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity":{
						"scriptName": "testsimple1",
						"parm" : {
							"name" : "normal1_set_testsimple1_element1",
							"state" : "element111"
						}
					}
				},
				{
					"extra": {
						"name": "element2",
						"description": "element2"
					},
					"entity":{
						"scriptName": "testsimple1",
						"parm" : {
							"name" : "normal1_set_testsimple1_element2",
							"state" : "element222"
						}
					}
				}
			]
		},
		"normal1_list_testsimple1": {
			"extra": {
				"status": "disabled1"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity":{
						"scriptName": "testsimple1",
						"parm" : {
							"name" : "normal1_list_testsimple1_element1",
							"state" : "element111"
						}
					}
				},
				{
					"extra": {
						"name": "element2",
						"description": "element2"
					},
					"entity":{
						"scriptName": "testsimple1",
						"parm" : {
							"name" : "normal1_list_testsimple1_element2",
							"state" : "element222"
						}
					}
				}
			]
		},
		"attachment": {
			"extra": {
				"status": "disabled1",
				"name": "parent attachment" 
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
		"valueStructure": {
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
	}
}
