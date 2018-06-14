package com.example.teachergradebook.data.repository;

import com.example.teachergradebook.data.Config;
import com.example.teachergradebook.data.api.ServerApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Денис on 15.03.2018.
 */


@Module
public class ApiServiceModule {
    private static final String BASE_URL = "http://gw.comsys.kpi.ua:10080/mygb/api/teacher/";

    @Provides
    @Named(BASE_URL)
    String provideBaseUrl() {
        return Config.API_HOST;
    }


    @Provides
    @Singleton
    Converter.Factory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    CallAdapter.Factory provideRxJavaAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@Named(BASE_URL) String baseUrl, Converter.Factory converterFactory,
                             CallAdapter.Factory adapterFactory) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(adapterFactory)
                .build();
    }

    @Provides
    @Singleton
    ServerApi provideQuestionService(Retrofit retrofit) {
        return retrofit.create(ServerApi.class);
    }
}
