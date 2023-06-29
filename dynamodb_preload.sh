#!/bin/bash
# Populating tables in AWS account

# Populating InstructorLeadClass table
aws dynamodb put-item \
    --table-name InstructorLeadClass  \
    --item \
	'{"classId": {"S": "9dff584d-98d0-45c5-9f43-b8eb1679295c"},
	"name": {"S": "Drew"},
	"description": {"S": "string"},
	"classType": {"S": "string"},
	"userId": {"S": "string"},
	"classCapacity": {"N": "0"},
	"dateTime":{"S": "2023-06-28T19:20:45.218"},
	"status": {"BOOL": true}}'

# Populating tables in local repository

# Populating InstructorLeadClass table
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name InstructorLeadClass  \
    --item \
	'{"classId": {"S": "9dff584d-98d0-45c5-9f43-b8eb1679295c"},
	"name": {"S": "Drew"},
	"description": {"S": "string"},
	"classType": {"S": "string"},
	"userId": {"S": "string"},
	"classCapacity": {"N": "0"},
	"dateTime":{"S": "2023-06-28T19:20:45.218"},
	"status": {"BOOL": true}}'

