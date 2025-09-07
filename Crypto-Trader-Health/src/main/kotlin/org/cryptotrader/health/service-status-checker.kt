@file:JvmName("ServiceStatusChecker")
package org.cryptotrader.health

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.cryptotrader.health.models.CryptoTraderService
import org.slf4j.LoggerFactory
import java.io.IOException

private val log = LoggerFactory.getLogger("org.cryptotrader.health.ServiceStatus")

private fun getUrl(service: CryptoTraderService): String {
    return "http://localhost:${service.port}/actuator/health";
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