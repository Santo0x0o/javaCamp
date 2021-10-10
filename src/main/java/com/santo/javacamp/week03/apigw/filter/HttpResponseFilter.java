package com.santo.javacamp.week03.apigw.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpResponseFilter {
   void filter(FullHttpResponse response);
}