package com.alex.example.security;

import io.grpc.ForwardingServerCallListener;
import io.grpc.ServerCall;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Spring Security context integration.
 *
 * This call listener is responsible for setting/un-setting {@link Authentication Authentication object}
 * in {@link SecurityContext Spring Security Context}.
 *
 * @param <ReqT> request type
 */
public class SpringSecurityServerCallListenter<ReqT> extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

    private final Authentication authentication;

    public SpringSecurityServerCallListenter(final ServerCall.Listener<ReqT> delegate, Authentication authentication) {
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
