@file:JvmName("GatewayAliases")
package org.cryptotrader.universal.library.events.alias

import java.util.concurrent.CompletableFuture

typealias GatewayReplies<Reply> = MutableMap<String, CompletableFuture<Reply>>