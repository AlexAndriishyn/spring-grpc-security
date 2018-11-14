package com.alex.example.security.grpc;

import com.alex.example.grpc.GreeterGrpc;
import com.alex.example.grpc.GreeterOuterClass;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.security.access.annotation.Secured;

@GRpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        super.sayHello(request, responseObserver);
    }
}
