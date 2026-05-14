package org.cryptotrader.universal.library.services

import org.cryptotrader.universal.library.entity.IdentifiableEntity

interface EntityHandler<Entity : IdentifiableEntity<Id>, Id> : EntityLookup<Entity, Id>, EntityPersistenceHandler<Entity, Id> {
    fun saveIfNew(entity: Entity): Entity
    fun deleteIfExists(entity: Entity): Boolean
}