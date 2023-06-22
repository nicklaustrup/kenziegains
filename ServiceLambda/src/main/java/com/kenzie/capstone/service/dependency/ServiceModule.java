package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.dao.ExampleDao;

import com.kenzie.capstone.service.dao.UserDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    //TODO add provide Users

    @Singleton
    @Provides
    @Inject
    public LambdaService provideLambdaService(@Named("ExampleDao") ExampleDao exampleDao) {
        return new LambdaService(exampleDao);
    }
//    @Singleton
//    @Provides
//    @Inject
//    public LambdaService provideUserService(@Named("UserDao") UserDao userDao) {
//        return new LambdaService(userDao);
//    }
}

