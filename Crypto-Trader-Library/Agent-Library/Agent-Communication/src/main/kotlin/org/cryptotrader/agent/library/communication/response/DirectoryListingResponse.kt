package org.cryptotrader.agent.library.communication.response

import org.cryptotrader.agent.library.model.FileMetadata

data class DirectoryListingResponse(
    val path: String,
    val entries: List<FileMetadata>
) : BaseAgentResponse()
