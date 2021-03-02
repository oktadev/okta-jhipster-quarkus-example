package com.mycompany.myapp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.quarkus.vertx.http.runtime.devmode.Json;
import io.smallrye.jwt.build.Jwt;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockOidcServerTestResource implements QuarkusTestResourceLifecycleManager {

    private final Logger log = LoggerFactory.getLogger(MockOidcServerTestResource.class);

    private WireMockServer server;

    private static RsaJsonWebKey key;

    private static RsaJsonWebKey getJwk() {
        if (key == null) {
            try {
                key = RsaJwkGenerator.generateJwk(2048);
                key.setUse("sig");
                key.setKeyId("1");
                key.setAlgorithm("RS256");
            } catch (JoseException e) {
                throw new RuntimeException("Impossible to create Json Web Key: " + e.getMessage());
            }
        }
        return key;
    }

    public static String getAccessToken(String username, Set<String> groups) {
        var key = getJwk();
        return Jwt.preferredUserName(username)
            .groups(groups)
            .jws()
            .keyId(key.getKeyId())
            .sign(key.getPrivateKey());
    }

    @Override
    public Map<String, String> start() {
        server = new WireMockServer();
        server.start();
        WireMock.stubFor(
            get(urlEqualTo("/jhipster-mock-oidc/.well-known/openid-configuration"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                    .withBody(Json.object()
                        .put("token_endpoint", server.baseUrl() + "/token")
                        .put("token_introspection_endpoint", server.baseUrl() + "/introspect")
                        .put("jwks_uri", server.baseUrl() + "/jwks")
                        .build()
                    )));

        WireMock.stubFor(
            get(urlEqualTo("/jwks"))
                .willReturn(aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON)
                    .withBody(new JsonWebKeySet(getJwk()).toJson())));

        log.info("Mock OIDC server started: {}", server.baseUrl());
        return Map.of(
            "quarkus.oidc.auth-server-url", server.baseUrl() + "/jhipster-mock-oidc"
        );
    }

    @Override
    public synchronized void stop() {
        if (server != null) {
            server.stop();
            log.info("Mock OIDC was shutdown: {}", server.baseUrl());
            server = null;
        }
    }
}
