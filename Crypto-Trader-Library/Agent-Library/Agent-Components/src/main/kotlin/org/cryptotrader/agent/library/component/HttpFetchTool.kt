package org.cryptotrader.agent.library.component

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.cryptotrader.agent.library.config.AgentConstraintsProperties
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springaicommunity.mcp.annotation.McpToolParam
import org.springframework.ai.tool.annotation.Tool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URI

@Component
class HttpFetchTool @Autowired constructor(
    private val properties: AgentConstraintsProperties,
    private val httpClient: CloseableHttpClient,
    private val httpGet: HttpGet
) {

    init {
        this.httpGet.setHeader("Accept", "text/plain, text/html;q=0.9")
        this.httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
    }


    // TODO: Send a request (GET, PUT, POST, etc.) to Crypto-Trader-Api (only
    //       internal). Major constraints needed.



    @Tool(description = "Fetch textual information from the internet. Use HTTPS host. Returns stripped text.")
    fun fetchWebsiteText(
        @McpToolParam(description = "The full HTTPS URL to fetch")
        url: String,
        @McpToolParam(description = "Maximum number of characters to return to avoid context window limits")
        maxChars: Int = 200_000
    ): String {
        val uri: URI = URI.create(url)

        require(uri.scheme == "https") { "Only https:// is allowed." }

        val hostName: String = uri.host ?: error("URL host is required.")
        require(hostName in properties.allowedHosts) { "Host is not allowlisted." }

        this.httpGet.uri = uri

        val response: CloseableHttpResponse = httpClient.execute(this.httpGet)
        val statusCode: Int = response.statusLine.statusCode
        require(statusCode in 200..299) { "HTTP $statusCode from host." }

        val contentType: String = response.getFirstHeader("Content-Type")?.value ?: ""
        var body: String = EntityUtils.toString(response.entity)

        val isHtml: Boolean = this.isHtml(contentType, body)

        if (isHtml) {
            body = this.stripHtml(body)
        }

        return if (body.length <= maxChars) body else body.substring(0, maxChars)
    }

    internal fun isHtml(
        contentType: String,
        body: String
    ): Boolean = contentType.contains("text/html") ||
        body.trim().take(100).contains("<!DOCTYPE html", ignoreCase = true) ||
        body.trim().take(100).contains("<html", ignoreCase = true)

    internal fun stripHtml(html: String): String {
        val doc: Document = Jsoup.parse(html)

        doc.select("script, style, noscript, svg, iframe, canvas").remove()
        val contentRoot: Element = doc.selectFirst("main, article, [role=main]") ?: doc.body()
        contentRoot.select("nav, footer, aside, header").remove()
        val links: List<String> = contentRoot.select("a[href]").map {
            it.attr("href")
        }.sortedBy { it.length }

        var parsedText: String = Jsoup.parse(html).text()
        if (links.isNotEmpty()) {
            parsedText += """
            Links:
            ${links.joinToString("\n")}
        """.trimIndent()
        }
        return parsedText
    }
}
