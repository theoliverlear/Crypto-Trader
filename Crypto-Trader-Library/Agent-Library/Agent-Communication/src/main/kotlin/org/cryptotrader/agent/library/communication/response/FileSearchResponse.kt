package org.cryptotrader.agent.library.communication.response

import org.cryptotrader.agent.library.model.FileSearchResult

data class FileSearchResponse(
    val results: List<FileSearchResult>
) : BaseAgentResponse()
