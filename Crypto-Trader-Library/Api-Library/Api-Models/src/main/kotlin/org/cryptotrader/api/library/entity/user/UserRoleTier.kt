package org.cryptotrader.api.library.entity.user

enum class UserRoleTier(val authority: String, val level: Int) {
    USER("ROLE_USER", 1),
    ADMIN("ROLE_ADMIN", 2),
    SUPER_ADMIN("ROLE_SUPER_ADMIN", 3);
    // TODO: Add final authority level, like a GOD or ROOT role.

    companion object {
        fun fromAuthority(authority: String): UserRoleTier? {
            return entries.find { it.authority == authority }
        }

        /**
         * Checks if this role has at least the required level of authority.
         */
        fun UserRoleTier.hasAtLeast(required: UserRoleTier): Boolean {
            return this.level >= required.level
        }
    }
}
