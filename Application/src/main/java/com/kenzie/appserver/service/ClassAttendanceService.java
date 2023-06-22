//package com.kenzie.appserver.service;
//
//import com.kenzie.appserver.controller.model.ClassAttendanceCreateRequest;
//import com.kenzie.appserver.repositories.ClassAttendanceRepository;
//import com.kenzie.appserver.repositories.model.ClassAttendanceCompositeId;
//import com.kenzie.appserver.service.model.ClassAttendance;
//import com.kenzie.appserver.service.model.Example;
//import com.kenzie.capstone.service.client.LambdaServiceClient;
//import com.kenzie.capstone.service.model.ClassAttendanceData;
//import com.kenzie.capstone.service.model.ClassAttendanceRecord;
//import com.kenzie.capstone.service.model.ExampleData;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ClassAttendanceService {
//    private ClassAttendanceRepository classAttendanceRepository;
//    private LambdaServiceClient lambdaServiceClient;
//
//    public ClassAttendanceService(ClassAttendanceRepository classAttendanceRepository, LambdaServiceClient lambdaServiceClient) {
//        this.classAttendanceRepository = classAttendanceRepository;
//        this.lambdaServiceClient = lambdaServiceClient;
//    }
//
////    public ClassAttendance findById(ClassAttendanceCompositeId compositeId) {
//
//        // Example getting data from the lambda
//     //   ExampleData dataFromLambda = lambdaServiceClient.getExampleData(id);
//
//        // Example getting data from the local repository
////        ClassAttendance dataFromDynamo = classAttendanceRepository
////                .findById(compositeId)
////                .map(classAttendance -> new ClassAttendance(
////                        classAttendance.getUserId(),
////                        classAttendance.getClassId(),
////                        classAttendance.getAttendanceStatus()))
////                .orElse(null);
//
////        return dataFromDynamo;
////    }
//
//    public ClassAttendance addNewClassAttendance(ClassAttendanceCreateRequest classAttendance) {
//
//        ClassAttendanceRecord classAttendanceRecord = new ClassAttendanceRecord();
//        classAttendanceRecord.setUserId(classAttendance.getUserId());
//        classAttendanceRecord.setClassId(classAttendance.getClassId());
//        classAttendanceRecord.setAttendanceStatus(classAttendance.getClassAttendance());
//
//
//
//        // Sending data to the lambda
//        ClassAttendanceData dataFromLambda = lambdaServiceClient.setClassAttendanceData(classAttendanceRecord);
//
//        // Example sending data to the local repository
////        ClassAttendanceRecord classAttendanceRecordLocal = new ClassAttendanceRecord();
////        classAttendanceRecordLocal.setUserId(dataFromLambda.getUserId());
////        classAttendanceRecordLocal.setClassId(dataFromLambda.getClassId());
////        classAttendanceRecordLocal.setAttendanceStatus(dataFromLambda.getAttendanceStatus());
////        classAttendanceRepository.save(classAttendanceRecordLocal);
//
//        ClassAttendance classAttendance1 = new ClassAttendance(dataFromLambda.getUserId(), dataFromLambda.getClassId(), dataFromLambda.getAttendanceStatus());
//
//
//
//        return classAttendance1;
//    }
//}
