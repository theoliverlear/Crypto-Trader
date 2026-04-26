package org.cryptotrader.chat.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class OpenAiChatCompletionRequest(
    val model: String? = null,
    val messages: List<OpenAiMessage> = emptyList(),
    val stream: Boolean? = false,
    @JsonProperty("stream_options")
    val streamOptions: OpenAiStreamOptions? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiStreamOptions(
    @JsonProperty("include_usage")
    val includeUsage: Boolean? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class OpenAiMessage(
    val role: String,
    val content: Any? = null,
    val name: String? = null,
    @JsonProperty("tool_call_id")
    val toolCallId: String? = null
)

data class OpenAiChatCompletionResponse(
    val id: String,
    @JsonProperty("object")
    val objectName: String = "chat.completion",
    val created: Long,
    val model: String,
    val choices: List<OpenAiChoice>,
    val usage: OpenAiUsage? = null
)

data class OpenAiChoice(
    val index: Int,
    val message: OpenAiAssistantMessage,
    @JsonProperty("finish_reason")
    val finishReason: String?
)

data class OpenAiAssistantMessage(
    val role: String = "assistant",
    val content: String = ""
)

data class OpenAiUsage(
    @JsonProperty("prompt_tokens")
    val promptTokens: Int,
    @JsonProperty("completion_tokens")
    val completionTokens: Int,
    @JsonProperty("total_tokens")
    val totalTokens: Int
)

data class OpenAiChatCompletionChunk(
    val id: String,
    @JsonProperty("object")
    val objectName: String = "chat.completion.chunk",
    val created: Long,
    val model: String,
    val choices: List<OpenAiChunkChoice>,
    val usage: OpenAiUsage? = null
)

data class OpenAiChunkChoice(
    val index: Int,
    val delta: OpenAiDelta,
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("finish_reason")
    val finishReason: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OpenAiDelta(
    val role: String? = null,
    val content: String? = null
)

data class OpenAiModelListResponse(
    @JsonProperty("object")
    val objectName: String = "list",
    val data: List<OpenAiModel>
)

data class OpenAiModel(
    val id: String,
    @JsonProperty("object")
    val objectName: String = "model",
    val created: Long,
    @JsonProperty("owned_by")
    val ownedBy: String = "openai"
)
