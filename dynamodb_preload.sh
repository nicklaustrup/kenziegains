#!/bin/bash
# Populating tables in AWS account
# Create InstructorLeadClass table
#!/bin/bash
# Populating tables in local repository only

# Check if InstructorLeadClass table exists
echo "Checking if InstructorLeadClass table exists..."
table_exists=$(aws dynamodb describe-table --endpoint-url http://localhost:8000 --table-name InstructorLeadClass 2>/dev/null || echo "false")

if [[ $table_exists == "false" ]]; then
    echo "Creating InstructorLeadClass table..."
    aws dynamodb create-table \
        --endpoint-url http://localhost:8000 \
        --table-name InstructorLeadClass \
        --attribute-definitions AttributeName=classId,AttributeType=S \
        --key-schema AttributeName=classId,KeyType=HASH \
        --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
    echo "InstructorLeadClass table created successfully."
else
    echo "InstructorLeadClass table already exists."
fi

# Check if User table exists
echo "Checking if User table exists..."
table_exists=$(aws dynamodb describe-table --endpoint-url http://localhost:8000 --table-name User 2>/dev/null || echo "false")

if [[ $table_exists == "false" ]]; then
    echo "Creating User table..."
    aws dynamodb create-table \
        --endpoint-url http://localhost:8000 \
        --table-name User \
        --attribute-definitions AttributeName=username,AttributeType=S \
        --key-schema AttributeName=username,KeyType=HASH \
        --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
    echo "User table created successfully."
else
    echo "User table already exists."
fi

# Check if ClassAttendance table exists
echo "Checking if ClassAttendance table exists..."
table_exists=$(aws dynamodb describe-table --endpoint-url http://localhost:8000 --table-name ClassAttendance 2>/dev/null || echo "false")

if [[ $table_exists == "false" ]]; then
    echo "Creating ClassAttendance table..."
    aws dynamodb create-table \
        --endpoint-url http://localhost:8000 \
        --table-name ClassAttendance \
        --attribute-definitions \
            AttributeName=userId,AttributeType=S \
            AttributeName=classId,AttributeType=S \
        --key-schema \
            AttributeName=userId,KeyType=HASH \
            AttributeName=classId,KeyType=RANGE \
        --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
    echo "ClassAttendance table created successfully."
else
    echo "ClassAttendance table already exists."
fi

echo "Adding Morning Yoga class..."
# Populating InstructorLeadClass table with multiple classes
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name InstructorLeadClass \
    --item \
    '{"classId": {"S": "9dff584d-98d0-45c5-9f43-b8eb1679295c"},
    "name": {"S": "Morning Yoga"},
    "description": {"S": "Start your day with rejuvenating yoga poses for all levels"},
    "classType": {"S": "Yoga"},
    "userId": {"S": "instructor-id-1"},
    "classCapacity": {"N": "20"},
    "dateTime":{"S": "2025-03-23T08:00:00.000"},
    "status": {"BOOL": true}}'
echo "Morning Yoga class added."

echo "Adding HIIT Challenge class..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name InstructorLeadClass \
    --item \
    '{"classId": {"S": "a5ff721e-bcde-4321-9876-c1d2e3f4g5h6"},
    "name": {"S": "HIIT Challenge"},
    "description": {"S": "High-intensity interval training for maximum fat burning"},
    "classType": {"S": "Cardio"},
    "userId": {"S": "instructor-id-2"},
    "classCapacity": {"N": "15"},
    "dateTime":{"S": "2025-03-23T17:30:00.000"},
    "status": {"BOOL": true}}'
echo "HIIT Challenge class added."

echo "Adding Boxing Fundamentals class..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name InstructorLeadClass \
    --item \
    '{"classId": {"S": "b7cc835f-abcd-9876-5432-d1e2f3a4b5c6"},
    "name": {"S": "Boxing Fundamentals"},
    "description": {"S": "Learn basic boxing techniques and combinations"},
    "classType": {"S": "Boxing"},
    "userId": {"S": "instructor-id-1"},
    "classCapacity": {"N": "12"},
    "dateTime":{"S": "2025-03-24T19:00:00.000"},
    "status": {"BOOL": true}}'
echo "Boxing Fundamentals class added."

echo "Adding Spinning Session class..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name InstructorLeadClass \
    --item \
    '{"classId": {"S": "c8dd946g-3456-7890-abcd-e1f2g3h4i5j6"},
    "name": {"S": "Spinning Session"},
    "description": {"S": "Intense indoor cycling workout with pumping music"},
    "classType": {"S": "Spinning"},
    "userId": {"S": "instructor-id-3"},
    "classCapacity": {"N": "25"},
    "dateTime":{"S": "2025-03-25T12:15:00.000"},
    "status": {"BOOL": true}}'
echo "Spinning Session class added."

echo "Adding Advanced Pilates class (cancelled)..."
# Add a class that's cancelled
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name InstructorLeadClass \
    --item \
    '{"classId": {"S": "d9ee057h-6543-2109-fedc-j1k2l3m4n5o6"},
    "name": {"S": "Advanced Pilates"},
    "description": {"S": "Challenging pilates workout for experienced practitioners"},
    "classType": {"S": "Pilates"},
    "userId": {"S": "instructor-id-2"},
    "classCapacity": {"N": "10"},
    "dateTime":{"S": "2025-03-26T09:30:00.000"},
    "status": {"BOOL": false}}'
echo "Advanced Pilates class added."

echo "Adding Admin user..."
# Add an admin user
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name User \
    --item \
    '{"userId": {"S": "admin-id-123"},
    "firstName": {"S": "Admin"},
    "lastName": {"S": "User"},
    "userType": {"S": "administrator"},
    "membership": {"S": "gold"},
    "status": {"S": "active"},
    "username": {"S": "admin"},
    "password": {"S": "password"}}'
echo "Admin user added."

echo "Adding John Smith instructor..."
# Add instructor users
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name User \
    --item \
    '{"userId": {"S": "instructor-id-1"},
    "firstName": {"S": "John"},
    "lastName": {"S": "Smith"},
    "userType": {"S": "instructor"},
    "membership": {"S": "gold"},
    "status": {"S": "active"},
    "username": {"S": "jsmith"},
    "password": {"S": "password"}}'
echo "John Smith instructor added."

echo "Adding Sarah Johnson instructor..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name User \
    --item \
    '{"userId": {"S": "instructor-id-2"},
    "firstName": {"S": "Sarah"},
    "lastName": {"S": "Johnson"},
    "userType": {"S": "instructor"},
    "membership": {"S": "gold"},
    "status": {"S": "active"},
    "username": {"S": "sjohnson"},
    "password": {"S": "password"}}'
echo "Sarah Johnson instructor added."

echo "Adding Mike Williams instructor..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name User \
    --item \
    '{"userId": {"S": "instructor-id-3"},
    "firstName": {"S": "Mike"},
    "lastName": {"S": "Williams"},
    "userType": {"S": "instructor"},
    "membership": {"S": "gold"},
    "status": {"S": "active"},
    "username": {"S": "mwilliams"},
    "password": {"S": "password"}}'
echo "Mike Williams instructor added."

echo "Adding Emily Davis gym member..."
# Add regular gym members
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name User \
    --item \
    '{"userId": {"S": "member-id-1"},
    "firstName": {"S": "Emily"},
    "lastName": {"S": "Davis"},
    "userType": {"S": "gymMember"},
    "membership": {"S": "gold"},
    "status": {"S": "active"},
    "username": {"S": "edavis"},
    "password": {"S": "password"}}'
echo "Emily Davis gym member added."

echo "Adding Robert Brown gym member..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name User \
    --item \
    '{"userId": {"S": "member-id-2"},
    "firstName": {"S": "Robert"},
    "lastName": {"S": "Brown"},
    "userType": {"S": "gymMember"},
    "membership": {"S": "regular"},
    "status": {"S": "active"},
    "username": {"S": "rbrown"},
    "password": {"S": "password"}}'
echo "Robert Brown gym member added."

echo "Adding Emily Davis attending Morning Yoga..."
# Add some class attendances
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name ClassAttendance \
    --item \
    '{"userId": {"S": "member-id-1"},
    "classId": {"S": "9dff584d-98d0-45c5-9f43-b8eb1679295c"},
    "attendanceStatus": {"S": "Attending"}}'
echo "Emily Davis attending Morning Yoga added."

echo "Adding Robert Brown attending Morning Yoga..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name ClassAttendance \
    --item \
    '{"userId": {"S": "member-id-2"},
    "classId": {"S": "9dff584d-98d0-45c5-9f43-b8eb1679295c"},
    "attendanceStatus": {"S": "Attending"}}'
echo "Robert Brown attending Morning Yoga added."

echo "Adding Emily Davis attending HIIT Challenge..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name ClassAttendance \
    --item \
    '{"userId": {"S": "member-id-1"},
    "classId": {"S": "a5ff721e-bcde-4321-9876-c1d2e3f4g5h6"},
    "attendanceStatus": {"S": "Attending"}}'
echo "Emily Davis attending HIIT Challenge added."

echo "Adding Robert Brown not attending Spinning Session..."
aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name ClassAttendance \
    --item \
    '{"userId": {"S": "member-id-2"},
    "classId": {"S": "c8dd946g-3456-7890-abcd-e1f2g3h4i5j6"},
    "attendanceStatus": {"S": "Not Attending"}}'
echo "Robert Brown not attending Spinning Session added."

echo "Tables populated with test data!"


# Populating tables in AWS account
# Populating InstructorLeadClass table
# aws dynamodb put-item \
#     --table-name InstructorLeadClass  \
#     --item \
# 	'{"classId": {"S": "9dff584d-98d0-45c5-9f43-b8eb1679295c"},
# 	"name": {"S": "Drew"},
# 	"description": {"S": "string"},
# 	"classType": {"S": "string"},
# 	"userId": {"S": "string"},
# 	"classCapacity": {"N": "0"},
# 	"dateTime":{"S": "2023-06-28T19:20:45.218"},
# 	"status": {"BOOL": true}}'

# Populating tables in local repository

