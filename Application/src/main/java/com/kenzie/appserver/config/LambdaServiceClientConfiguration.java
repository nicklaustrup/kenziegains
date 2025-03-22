package com.kenzie.appserver.config;

import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaServiceClientConfiguration {

    @Bean
    public LambdaServiceClient referralServiceClient() {
        return new LambdaServiceClient();
    }
}

// package com.kenzie.appserver.config;

// import com.kenzie.capstone.service.client.LambdaServiceClient;
// import com.kenzie.appserver.service.mock.MockLambdaServiceClient;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;

// @Configuration
// public class LambdaServiceClientConfiguration {

//     @Bean
//     @Profile("!local")
//     public LambdaServiceClient lambdaServiceClient() {
//         return new LambdaServiceClient();
//     }

//     @Bean
//     @Profile("local")
//     public LambdaServiceClient mockLambdaServiceClient() {
//         return new MockLambdaServiceClient();
//     }

