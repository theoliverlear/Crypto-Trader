import java.security.SecureRandom
import java.util.Base64

fun generate(bytes: Int = 32): String {
    val buffer = ByteArray(bytes)
    SecureRandom().nextBytes(buffer)
    return Base64.getEncoder().encodeToString(buffer)
}

println(generate(32))