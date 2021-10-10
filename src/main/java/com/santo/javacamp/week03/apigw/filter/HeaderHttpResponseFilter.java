package com.santo.javacamp.week03.apigw.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("ranid", "U2FsdGVkX18tTp5vBU8R9+MpO65pigcoDK30nsV3sgeLbXy1ditunnvsR7RGBMbY");
    }
}