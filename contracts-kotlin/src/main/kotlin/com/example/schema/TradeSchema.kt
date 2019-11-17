package com.example.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * The family of schemas for TradeState.
 */
object TradeSchema

/**
 * An IOUState schema.
 */
object TradeSchemaV1 : MappedSchema(
        schemaFamily = TradeSchema.javaClass,
        version = 1,
        mappedTypes = listOf(PersistentTrade::class.java)) {
    @Entity
    @Table(name = "trade_states")
    class PersistentTrade(
            @Column(name = "buyer")
            var buyerName: String,

            @Column(name = "seller")
            var sellerName: String,

            @Column(name = "price")
            var price: Int,

            @Column(name = "asset")
            var asset: String,

            @Column(name = "linear_id")
            var linearId: UUID
    ) : PersistentState() {
        // Default constructor required by hibernate.
        constructor(): this("", "", 0, "", UUID.randomUUID())
    }
}