package com.alex.example.security

import com.alex.example.security.jwt.KeyProvider
import org.springframework.boot.test.context.SpringBootTest

import javax.inject.Inject

/**
 * Created by alex.
 */
@SpringBootTest
class GreeterServiceTest {

    @Inject KeyProvider keyProvider

    def 'throws'() {

    }

    def 'does not throw'() {

    }

    def createJwt() {

    }

    def getStub() {

    }
}
