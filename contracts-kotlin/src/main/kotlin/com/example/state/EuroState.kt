
import net.corda.core.contracts.*
import net.corda.core.identity.AbstractParty
import net.corda.core.transactions.LedgerTransaction

@BelongsToContract(EuroContract::class)
data class EuroState(
        override val amount: Amount<Long>,
        val owner: AbstractParty,
        val issuer: AbstractParty
        ): FungibleState<Long> {
    override val participants: List<AbstractParty>
        get() = listOf(owner)
}

class EuroContract: Contract {
    override fun verify(tx: LedgerTransaction) {

    }
}

class IssueEuroCommand: CommandData

class MoveEuroCommand: CommandData
