package org.cryptotrader.universal.library.services

import org.cryptotrader.universal.library.entity.Identifiable
import org.cryptotrader.universal.library.model.exception.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.aop.support.AopUtils
import org.springframework.core.ResolvableType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

abstract class BaseEntityService<Entity : Identifiable<Id>, Id : Any, Repository : JpaRepository<Entity, Id>>(
    val repository: Repository
) : EntityHandler<Entity, Id> {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)
    private val entityType: Class<out Entity> by lazy { this.resolveEntityType() }

    override fun saveIfNew(entity: Entity): Entity {
        if (!this.exists(entity)) {
            return this.save(entity)
        }
        log.info("{} with ID {} already exists, skipping save.",
            this.getEntityName(entity), entity.id)
        return entity
    }

    override fun deleteIfExists(entity: Entity): Boolean {
        if (this.exists(entity)) {
            return this.delete(entity)
        }
        log.warn("Entity with ID {} does not exist, cannot delete.", entity.id)
        return false
    }

    override fun findById(id: Id): Optional<Entity> {
        log.debug("Finding {} with ID {}.", this.getEntityName(), id)
        return this.repository.findById(id)
    }

    override fun findByIds(ids: List<Id>): List<Entity> {
        log.debug("Finding {} {}.", ids.size, this.getEntityName())
        return this.repository.findAllById(ids)
    }

    override fun findAll(): List<Entity> {
        log.debug("Finding all {} entities.", this.getEntityName())
        return this.repository.findAll()
    }

    override fun exists(entity: Entity): Boolean {
        log.debug("Checking existence of {} with ID {}.", this.getEntityName(entity), entity.id)
        return this.repository.existsById(entity.id)
    }

    override fun existsById(id: Id): Boolean {
        log.debug("Checking existence of {} with ID {}.", this.getEntityName(), id)
        return this.repository.existsById(id)
    }

    override fun count(): Long {
        log.info("Counting {} entities.", this.getEntityName())
        return this.repository.count()
    }

    override fun save(entity: Entity): Entity {
        return try {
            log.info("Saving new {} entity.", this.getEntityName(entity))
            this.repository.save(entity)
        } catch (exception: IllegalArgumentException) {
            log.warn("Failed to save {} with ID {}.", this.getEntityName(entity), entity.id)
            throw exception
        }
    }

    override fun saveAll(entities: List<Entity>): List<Entity> {
        return try {
            log.info("Saving {} {} entities.", entities.size, this.getEntityName(entities.first()))
            this.repository.saveAll(entities)
        } catch (exception: IllegalArgumentException) {
            log.error("Failed to save {} {} entities.", entities.size, this.getEntityName(entities.first()), exception)
            throw exception
        }
    }

    override fun delete(entity: Entity): Boolean {
        return try {
            log.info("Deleting {} with ID {}.", this.getEntityName(entity), entity.id)
            this.repository.delete(entity)
            true
        } catch (exception: IllegalArgumentException) {
            log.error("Failed to delete {} with ID {}.", this.getEntityName(entity), entity.id, exception)
            false
        }
    }

    override fun deleteById(id: Id): Boolean {
        if (this.existsById(id)) {
            return try {
                log.info("Deleting {} with ID {}.", this.getEntityName(), id)
                this.repository.deleteById(id)
                true
            } catch (exception: IllegalArgumentException) {
                log.error("Failed to delete {} with ID {}.", this.getEntityName(), id, exception)
                false
            }
        }
        log.warn("Entity with ID {} does not exist, cannot delete.", id)
        return false
    }

    override fun deleteAll(entities: List<Entity>): Boolean {
        val allExist: Boolean = entities.all { this.exists(it) }
        if (allExist) {
            return try {
                log.info(
                    "Deleting {} {}.",
                    entities.size,
                    this.getEntityName(entities.first())
                )
                this.repository.deleteAll(entities)
                true
            } catch (exception: IllegalArgumentException) {
                log.error("Failed to delete {} {}.", entities.size, this.getEntityName(entities.first()), exception)
                false
            }
        }
        log.error("Failed to delete {} {}, not all entities exist.", entities.size, this.getEntityName(entities.first()))
        return false
    }

    override fun update(entity: Entity): Entity {
        if (!this.exists(entity)) {
            log.info("Updating {} with ID {}.", this.getEntityName(entity), entity.id)
            return this.save(entity)
        }
        log.error("Failed to update {} with ID {}, entity does not exist.", this.getEntityName(entity), entity.id)
        throw EntityNotFoundException(entity)
    }

    override fun updateAll(entities: List<Entity>): List<Entity> {
        val allExist: Boolean = entities.all { this.exists(it) }
        if (allExist) {
            return this.saveAll(entities)
        }
        log.error("Failed to update {} {}, not all entities exist.", entities.size, this.getEntityName(entities.first()))
        throw EntityNotFoundException()
    }

    private fun getEntityName(entity: Entity): String {
        return AopUtils.getTargetClass(entity).simpleName ?: "UnknownEntity"
    }

    private fun getEntityName(): String {
        return this.entityType.simpleName ?: "UnknownEntity"
    }

    @Suppress("UNCHECKED_CAST")
    private fun resolveEntityType(): Class<out Entity> {
        val targetClass: Class<*> = AopUtils.getTargetClass(this)
        val resolvedType: Class<*> = ResolvableType.forClass(targetClass)
            .`as`(BaseEntityService::class.java)
            .getGeneric(0)
            .resolve()
            ?: throw IllegalStateException("Unable to resolve entity type for ${targetClass.name}")

        return resolvedType as Class<out Entity>
    }
}
