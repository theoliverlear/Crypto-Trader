package org.cryptotrader.universal.library.services

import org.cryptotrader.universal.library.entity.IdentifiableEntity
import org.cryptotrader.universal.library.model.exception.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

abstract class BaseEntityService<Entity : IdentifiableEntity<Id>, Id, Repository : JpaRepository<Entity, Id>>(
    val repository: Repository
) : EntityHandler<Entity, Id> {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun saveIfNew(entity: Entity): Entity {
        if (!this.exists(entity)) {
            return this.save(entity)
        }
        return entity
    }

    override fun deleteIfExists(entity: Entity): Boolean {
        if (this.exists(entity)) {
            return this.delete(entity)
        }
        return false
    }

    override fun findById(id: Id): Optional<Entity> {
        return this.repository.findById(id)
    }

    override fun findByIds(ids: List<Id>): List<Entity> {
        return this.repository.findAllById(ids)
    }

    override fun findAll(): List<Entity> {
        return this.repository.findAll()
    }

    override fun exists(entity: Entity): Boolean {
        return this.repository.existsById(entity.id)
    }

    override fun existsById(id: Id): Boolean {
        return this.repository.existsById(id)
    }

    override fun count(): Long {
        return this.repository.count()
    }

    override fun save(entity: Entity): Entity {
        return this.repository.save(entity)
    }

    override fun saveAll(entities: List<Entity>): List<Entity> {
        return this.repository.saveAll(entities)
    }

    override fun delete(entity: Entity): Boolean {
        if (this.exists(entity)) {
            this.repository.delete(entity)
            return true
        }
        return false
    }

    override fun deleteById(id: Id): Boolean {
        if (this.existsById(id)) {
            this.repository.deleteById(id)
            return true
        }
        return false
    }

    override fun deleteAll(entities: List<Entity>): Boolean {
        val allExist: Boolean = entities.all { this.exists(it) }
        if (allExist) {
            this.repository.deleteAll(entities)
            return true
        }
        return false
    }

    override fun update(entity: Entity): Entity {
        if (!this.exists(entity)) {
            return this.save(entity)
        }
        throw EntityNotFoundException(entity)
    }

    override fun updateAll(entities: List<Entity>): List<Entity> {
        val allExist: Boolean = entities.all { this.exists(it) }
        if (allExist) {
            return this.saveAll(entities)
        }
        throw EntityNotFoundException()
    }
}