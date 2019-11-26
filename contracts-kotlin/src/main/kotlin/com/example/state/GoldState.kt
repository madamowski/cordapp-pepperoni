
import net.corda.core.contracts.*
import net.corda.core.identity.AbstractParty
import net.corda.core.transactions.LedgerTransaction

@BelongsToContract(GoldContract::class)
data class GoldState(
        override val amount: Amount<Long>,
        val owner: AbstractParty,
        val issuer: AbstractParty
        ): FungibleState<Long> {
    override val participants: List<AbstractParty>
        get() = listOf(owner)
}

class GoldContract: Contract {
    override fun verify(tx: LedgerTransaction) {

    }
}

class IssueGoldCommand: CommandData

class MoveGoldCommand: CommandData
