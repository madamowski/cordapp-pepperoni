import net.corda.core.contracts.Amount
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

// TODO to be invoked from the shell to issue assets and distribute across parties

@StartableByRPC
class IssueFlow(val euroAmount: Amount<Long>, val goldAmount: Amount<Long>): FlowLogic<Unit>() {
    override fun call() {
        val goldTx = TransactionBuilder()
                .addOutputState(GoldState(goldAmount, ourIdentity, ourIdentity))
                .addCommand(IssueGoldCommand(), ourIdentity.owningKey)
        val euroTx = TransactionBuilder()
                .addOutputState(EuroState(euroAmount, ourIdentity, ourIdentity))
                .addCommand(IssueEuroCommand(), ourIdentity.owningKey)
        val goldTxSigned = serviceHub.signInitialTransaction(goldTx)
        val euroTxSigned = serviceHub.signInitialTransaction(euroTx)
        subFlow(FinalityFlow(goldTxSigned, emptyList()))
        subFlow(FinalityFlow(euroTxSigned, emptyList()))

        TransactionBuilder()
                .addInputState(goldTxSigned.coreTransaction.outRef<GoldState>(0))
                .addOutputState(GoldState(goldAmount, serviceHub.networkMapCache.getPeerByLegalName("")))
    }
}

@InitiatingFlow
class TransferAssetFlow(val tx: SignedTransaction, val counterParty: String): FlowLogic<Unit>() {
    override fun call() {

    }
}

@InitiatedBy(TransferAssetFlow::class)
class AcceptAssetFlow: FlowLogic<Unit>() {
    override fun call() {

    }
}
