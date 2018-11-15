package com.alex.example.security;

import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@GRpcGlobalInterceptor
public class JWTServerInterceptor implements ServerInterceptor {

    private final JWTValidator jwtValidator;

    @Inject
    public JWTServerInterceptor(JWTValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    ) {
        try {
            jwtValidator.validate(headers, getClaims());
        } catch (JWTValidationException ex) {
            call.close(Status.UNAUTHENTICATED, headers);
        }

        return next.startCall(call, headers);
    }

    private Map<String, String> getClaims() {
        Map<String, String> claims = new HashMap<>();

        claims.put("iss", "leonardo");
        claims.put("sub", "alex");
        claims.put("aud", "service/greeter");

        return claims;
    }
}
