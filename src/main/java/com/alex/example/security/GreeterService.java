package com.alex.example.security;

import com.alex.example.grpc.GreeterGrpc;
import com.alex.example.grpc.GreeterOuterClass;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String name = request.getName();

        GreeterOuterClass.HelloReply helloReply = GreeterOuterClass.HelloReply.newBuilder()
            .setMessage("hello, " + name)
            .build();

        responseObserver.onNext(helloReply);
    }
}
