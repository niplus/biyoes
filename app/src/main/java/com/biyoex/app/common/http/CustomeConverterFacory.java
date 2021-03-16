package com.biyoex.app.common.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomeConverterFacory extends Converter.Factory {
    private final Gson gson;

    public static CustomeConverterFacory create() {
        return create(new Gson());
    }

    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static CustomeConverterFacory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new CustomeConverterFacory(gson);
    }

    private CustomeConverterFacory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return new StringResponseBodyConverter();
        } else {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new CustomeResponseBodyConverter<>(gson, adapter);
        }
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomeRequestBodyConverter<>(gson, adapter);
    }
}
