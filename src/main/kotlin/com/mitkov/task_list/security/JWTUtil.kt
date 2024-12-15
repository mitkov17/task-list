package com.mitkov.task_list.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.mitkov.task_list.entities.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
class JWTUtil(
    @Value("{jwt-secret}") private val secret: String
) {
    fun generateToken(username: String, role: Role): String {
        val expirationTime = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant())

        return JWT.create()
            .withSubject("User details")
            .withClaim("username", username)
            .withClaim("role", role.name)
            .withIssuedAt(Date())
            .withIssuer("TaskListApp")
            .withExpiresAt(expirationTime)
            .sign(Algorithm.HMAC256(secret))
    }

    fun validateTokenAndRetrieveClaims(token: String): Pair<String, String> {
        val verifier: JWTVerifier = JWT.require(Algorithm.HMAC256(secret))
            .withSubject("User details")
            .withIssuer("TaskListApp")
            .build()

        val jwt = verifier.verify(token)
        val username = jwt.getClaim("username").asString()
        val role = jwt.getClaim("role").asString()

        return username to role
    }
}
