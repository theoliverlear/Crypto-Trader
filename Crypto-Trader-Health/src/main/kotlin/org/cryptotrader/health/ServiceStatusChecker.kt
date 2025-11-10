@file:JvmName("ServiceStatusChecker")
package org.cryptotrader.health

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.cryptotrader.health.models.CryptoTraderService
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URI

private const val BASE_OVERRIDE_PROP = "ct.health.base" // e.g., "http://localhost:12345"
private const val BASE_OVERRIDE_ENV = "CT_HEALTH_BASE"

private val log = LoggerFactory.getLogger("org.cryptotrader.health.ServiceStatus")

private fun getBaseOverride(): String? {
    return System.getProperty(BASE_OVERRIDE_PROP)
        ?: System.getenv(BASE_OVERRIDE_ENV)
}

internal fun getUrl(service: CryptoTraderService): String {
    val host = if (service === CryptoTraderService.DATA) {
        System.getenv("CT_DATA_HOST")
    } else {
        "localhost"
    }
    val base = getBaseOverride()
    return if (base != null) {
        URI.create(base).resolve("/actuator/health").toString()
    } else {
        "http://${host}:${service.port}/actuator/health"
    }
}

private fun getHttpRequest(url: String): HttpGet {
    return HttpGet(url)
}

private fun getHttpClient(): HttpClient {
    return HttpClientBuilder.create().build()
}

fun isServiceAlive(service: CryptoTraderService): Boolean {
    log.info("Checking status of service: {}...", service)
    val validCode: Int = 200
    val client: HttpClient = getHttpClient()
    val request: HttpGet = getHttpRequest(getUrl(service))
    try {
        val response: HttpResponse = client.execute(request) ?: return false
        val isAlive: Boolean = validCode == response.statusLine.statusCode
        log.info("Service: {} is alive: {}", service, isAlive)
        return isAlive
    } catch (exception: IOException) {
        log.error("Error checking status of service: {}", service, exception)
        return false
    }
}