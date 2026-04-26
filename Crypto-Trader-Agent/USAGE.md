# 🚀 Using Crypto-Trader MCP Tools

This guide explains how to connect and use the
**Model Context Protocol (MCP)** tools implemented in `Crypto-Trader-Agent`.

## 🛠️ Available Tools

| Tool Name            | Purpose                     | Example Use Case                                     |
|:---------------------|:----------------------------|:-----------------------------------------------------|
| `executeReadOnlySql` | Query the 2B+ row dataset   | "Why is my currency_history query slow?"             |
| `describeTable`      | Get column info for a table | "Show me the columns in the trades table."           |
| `readProjectFile`    | Read specific source files  | "Why aren't transactions linking to trades?"         |
| `listDirectory`      | Explore project structure   | "Show me the structure of the data-services module." |
| `fetchUrl`           | Retrieve external docs/info | "What's the best indexing strategy for Postgres 17?" |

---

## ⚙️ Setup & Configuration

### 1. Build the Agent
Ensure you have the `Crypto-Trader-Agent` JAR built.
```bash
mvn clean install -pl :crypto-trader-agent -DskipTests
```

### 2. Configure

#### 🌐 Remote (HTTP/SSE) - *Required Transport*
Ensure `stdio: false` and `protocol: SSE` are set in the agent's `application.yml`.
Use the following JSON configuration:
```json
{
  "mcpServers": {
    "crypto-trader-agent": {
      "url": "http://localhost:8089/mcp/webmvc"
    }
  }
}
```

#### 📁 Local (STDIO) - *Alternative*
If you prefer the IDE to manage the lifecycle, ensure `stdio: true` is set in
the agent's `application.yml`.
```json
{
  "mcpServers": {
    "crypto-trader-agent": {
      "command": "/.jdks/openjdk-23.0.1/bin/java",
      "args": ["-jar", "/Crypto-Trader/Crypto-Trader-Agent/target/crypto-trader-agent-0.3.0.jar"]
    }
  }
}
```