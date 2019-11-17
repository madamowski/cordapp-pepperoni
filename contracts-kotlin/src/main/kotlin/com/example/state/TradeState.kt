package com.example.state

import com.example.contract.TradeContract
import com.example.schema.TradeSchemaV1
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

/**
 * The state object recording Trade agreements between two parties.
 *
 * A state must implement [ContractState] or one of its descendants.
 *
 * @param price the value of the Trade.
 * @param buyer the party issuing the Trade.
 * @param seller the party receiving and approving the Trade.
 */
@BelongsToContract(TradeContract::class)
data class TradeState(val price: Int,
                      val asset: String,
                      val buyer: Party,
                      val seller: Party,
                      override val linearId: UniqueIdentifier = UniqueIdentifier()) :
        LinearState, QueryableState {
    /** The public keys of the involved parties. */
    override val participants: List<AbstractParty> get() = listOf(buyer, seller)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        return when (schema) {
            is TradeSchemaV1 -> TradeSchemaV1.PersistentTrade(
                    this.buyer.name.toString(),
                    this.seller.name.toString(),
                    this.price,
                    this.asset,
                    this.linearId.id
            )
            else -> throw IllegalArgumentException("Unrecognised schema $schema")
        }
    }

    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(TradeSchemaV1)
}
