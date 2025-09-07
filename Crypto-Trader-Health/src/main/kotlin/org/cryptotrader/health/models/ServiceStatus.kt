@file:JvmName("ServiceStatus")

package org.cryptotrader.health.models

enum class ServiceStatus(isAlive: Boolean) {
    ALIVE(true),
    DEAD(false),
    MAINTENANCE(false);
}