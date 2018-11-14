package com.alex.example.security.grpc;

import com.alex.example.security.jwt.JWTParser;
import io.grpc.*;
import io.jsonwebtoken.Claims;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;


import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@GRpcGlobalInterceptor
public class AuthenticationInterceptor implements ServerInterceptor {

    private final Metadata.Key<String> AUTHENTICATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);

    private final JWTParser jwtParser;
    private final AuthenticationManager authenticationManager;

    @Inject
    public AuthenticationInterceptor(
        AuthenticationManager authenticationManager,
        JWTParser jwtParser
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtParser = jwtParser;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    ) {
        // Locate JWT in metadata: "Authorization: Bearer ${JWT}".
        String jwt = headers.get(AUTHENTICATION_METADATA_KEY);

        if (jwt == null) {
            try {
                return next.startCall(call, headers);
            } catch (AccessDeniedException ex) {
                throw new BadCredentialsException("jwt token is missing");
            }
        }

        // Validate the JWT.
        // General:
        //  - "iss": principal that issued the JWT(authorization service)
        //  - "sub": principal that is the subject of the JWT(user id)
        //  - "aud": ?
        // Dates:
        //  - "exp": the JWT expiry date, must be no earlier than the current date
        //  - "nbf": time before which the JWT must not be accepted, must be earlier than the current date
        //  - "iat": when the JWT was issued, can determine the age of the token with this
       jwtParser.validate(jwt);

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

    /**
     * IGNORE THIS FOR NOW.
     *
     * This call listener is responsible for setting/un-setting {@link Authentication Authentication object}
     * in {@link SecurityContext Spring Security Context}.
     *
     * @param <ReqT> request type
     */
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
