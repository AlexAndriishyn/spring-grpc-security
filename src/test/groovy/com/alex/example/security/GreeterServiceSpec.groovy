package com.alex.example.security

import com.alex.example.grpc.GreeterGrpc
import com.alex.example.grpc.GreeterOuterClass
import io.grpc.ManagedChannelBuilder
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import io.grpc.testing.GrpcServerRule
import org.junit.Rule
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER

/**
 * Created by alex.
 */
@SpringBootTest(classes = Application.class)
class GreeterServiceSpec extends Specification {

    @Shared
    JWTStore jwtStore

    @Rule
    GrpcServerRule grpcServerRule = new GrpcServerRule()

    @Inject
    GreeterService greeterService

    GreeterGrpc.GreeterBlockingStub stub

    def setupSpec() {
        jwtStore = new JWTStore()
    }

    def setup() {
        grpcServerRule.getServiceRegistry().addService(greeterService)
        stub = GreeterGrpc.newBlockingStub(grpcServerRule.getChannel())
//        stub = GreeterGrpc.newBlockingStub(ManagedChannelBuilder.forAddress('localhost', 6565).build())
    }

    def 'valid jwt'() {
        given:
        MetadataUtils.attachHeaders(stub, getMetadataWithJwt(jwtStore.createJWT()))
        def request = GreeterOuterClass.HelloRequest.newBuilder().setName('alex').build()

        when:
        def response = stub.sayHello(request)

        then:
        response.getMessage() == 'hello, alex'
    }

    def getMetadataWithJwt(String jwt) {
        def metadata = new Metadata()

        metadata.put(Metadata.Key.of('Authorization', ASCII_STRING_MARSHALLER), jwt)

        return metadata
    }
}
