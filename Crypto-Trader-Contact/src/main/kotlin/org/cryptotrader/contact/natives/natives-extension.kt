@file:JvmName("NativesExtension")

package org.cryptotrader.contact.natives

// TODO: This will likely be migrated to a Kotlin library.
fun String.normalized(): String = this.replace("\r\n", "\n")