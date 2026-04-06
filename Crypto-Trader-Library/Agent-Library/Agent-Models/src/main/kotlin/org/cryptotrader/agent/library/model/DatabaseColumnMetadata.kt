package org.cryptotrader.agent.library.model

// TODO: Might be universal, and extendable.
data class DatabaseColumnMetadata(
    val name: String,
    val type: String,
    val nullable: Boolean
)
