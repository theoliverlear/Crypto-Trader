/**
 * Java Platform Module for lightweight HTTP and DPoP helper scripts used by the API layer.
 *
 * What lives here:
 * - org.cryptotrader.api.library.scripts.http: small, side‑effect‑free HTTP utilities (cookie read, URL building,
 *   consistent 401 writer) used by filters and controllers.
 * - org.cryptotrader.api.library.scripts.dpop: minimal DPoP helpers (JWK thumbprint calc, basic structure checks)
 *   kept isolated from the stronger verifier service to allow incremental adoption.
 *
 * Notes for maintainers:
 * - This module intentionally avoids heavy dependencies and is safe to reuse across services.
 * - The full cryptographic DPoP verification is implemented in the infrastructure/services modules; use those in
 *   request filters. The helpers here are for convenience and small glue logic only.
 */
module org.cryptotrader.api.library.scripts {
    requires spring.beans;
    requires spring.context;
    requires spring.jdbc;
    requires spring.tx;
    requires spring.web;
    requires spring.boot.autoconfigure;
    requires org.apache.tomcat.embed.core;
    requires org.slf4j;
    requires kotlin.stdlib;
    requires jjwt.api;
    requires com.auth0.jwt;
    requires java.sql;
    requires static lombok;
    
    exports org.cryptotrader.api.library.scripts.http;
    exports org.cryptotrader.api.library.scripts.dpop;
}