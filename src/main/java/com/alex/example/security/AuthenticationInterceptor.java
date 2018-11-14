package com.alex.example.security;

import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;

@GRpcGlobalInterceptor
public class AuthenticationInterceptor implements ServerInterceptor {

    private final AuthenticationManager authenticationManager;

    @Inject
    public AuthenticationInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    ) {
        // locate JWT in metadata: "Authorization: Bearer ${JWT}".
        // read JWT with jjwt.
        // validate JWT.
        //  - based on claims e.g "expiry date".
        //    this is clearly not sufficient since we must be sure that this is our token
        //    access "User" service to verify that the user/password combo from JWT exists?
        //  - based on remote authorization server's "validate" method.
        //    seems to be nicely decoupled from the authorization server implementation
        //    to achieve this, Spring Security offers "RemoteTokenServices" class
        // transform JWT into Spring Security Authentication object
        //  this can be used to provide an application-wide security context for the current user
        Authentication authentication = null;

        return new AuthenticationServerCallListener<>(next.startCall(call, headers), authentication);
    }

    private class AuthenticationServerCallListener<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

        private final Authentication authentication;

        public AuthenticationServerCallListener(final ServerCall.Listener<ReqT> delegate, Authentication authentication) {
            super(delegate);
            this.authentication = authentication;
        }

        @Override
        public void onMessage(ReqT message) {
            try {
                SecurityContextHolder.getContext().setAuthentication(this.authentication);
                super.onMessage(message);
            } finally {
                SecurityContextHolder.clearContext();
            }
        }

        @Override
        public void onHalfClose() {
            try {
                SecurityContextHolder.getContext().setAuthentication(this.authentication);
                super.onHalfClose();
            } finally {
                SecurityContextHolder.clearContext();
            }
        }

        @Override
        public void onCancel() {
            try {
                SecurityContextHolder.getContext().setAuthentication(this.authentication);
                super.onCancel();
            } finally {
                SecurityContextHolder.clearContext();
            }
        }

        @Override
        public void onComplete() {
            try {
                SecurityContextHolder.getContext().setAuthentication(this.authentication);
                super.onComplete();
            } finally {
                SecurityContextHolder.clearContext();
            }
        }

        @Override
        public void onReady() {
            try {
                SecurityContextHolder.getContext().setAuthentication(this.authentication);
                super.onReady();
            } finally {
                SecurityContextHolder.clearContext();
            }
        }
    }
}
