package org.cryptotrader.universal.library.services

import org.cryptotrader.universal.library.entity.IdentifiableEntity
import java.util.Optional

interface EntityLookup<Entity : IdentifiableEntity<Id>, Id> {
    fun findById(id: Id): Optional<Entity>
    fun findByIds(ids: List<Id>): List<Entity>
    fun findAll(): List<Entity>
    fun exists(entity: Entity): Boolean
    fun existsById(id: Id): Boolean
    fun count(): Long
}