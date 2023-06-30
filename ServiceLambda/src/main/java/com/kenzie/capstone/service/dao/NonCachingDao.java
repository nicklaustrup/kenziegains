//package com.kenzie.capstone.service.dao;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.*;
//import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
//import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
//import com.google.common.collect.ImmutableMap;
//import com.kenzie.capstone.service.model.ClassAttendanceRecord;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.List;


//******* Did not realize that all of this was already setup by my team in example dao
//
//public class NonCachingDao {
//
//        private DynamoDBMapper mapper;
//        static final Logger log = LogManager.getLogger();
//
//        /**
//         * Allows access to and manipulation of Match objects from the data store.
//         * @param mapper Access to DynamoDB
//         */
//        public NonCachingDao(DynamoDBMapper mapper) {
//            this.mapper = mapper;
//        }
//
//        public ClassAttendanceRecord addReferral(ClassAttendanceRecord attendanceRecord) {
//            try {
//                mapper.save(attendanceRecord, new DynamoDBSaveExpression()
//                        .withExpected(ImmutableMap.of(
//                                "userId",
//                                new ExpectedAttributeValue().withExists(false)
//                        )));
//            } catch (ConditionalCheckFailedException e) {
//                throw new InvalidDataException("Customer has already been referred");
//            }
//
//            return attendanceRecord;
//        }
//
//        public boolean deleteReferral(ClassAttendanceRecord attendanceRecord) {
//            try {
//                mapper.delete(attendanceRecord, new DynamoDBDeleteExpression()
//                        .withExpected(ImmutableMap.of(
//                                "CustomerId",
//                                new ExpectedAttributeValue().withValue(new AttributeValue(attendanceRecord.getUserId())).withExists(true)
//                        )));
//            } catch (AmazonDynamoDBException e) {
//                log.info(e.getMessage());
//                log.info(e.getStackTrace());
//                return false;
//            }
//
//            return true;
//        }
//
//        public List<ClassAttendanceRecord> GetUserId(String userId) {
//            ClassAttendanceRecord attendanceRecord = new ClassAttendanceRecord();
//            attendanceRecord.setUserId(userId);
//
//            DynamoDBQueryExpression<ClassAttendanceRecord> queryExpression = new DynamoDBQueryExpression<ClassAttendanceRecord>()
//                    .withHashKeyValues(attendanceRecord)
//                    .withIndexName("UserIdIndex")
//                    .withConsistentRead(false);
//
//            return mapper.query(ClassAttendanceRecord.class, queryExpression);
//        }
//
////        public List<ClassAttendanceRecord> findUsersWithoutReferrerId() {
////            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
////                    .withFilterExpression("attribute_not_exists(ReferrerId)");
////
////            return mapper.scan(ClassAttendanceRecord.class, scanExpression);
////        }
//    }
//
//}
