package com.example.test.contract

import com.example.contract.TradeContract
import com.example.state.TradeState
import net.corda.core.identity.CordaX500Name
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.Test

class TradeContractTests {
    private val ledgerServices = MockServices(listOf("com.example.contract", "com.example.flow"))
    private val megaCorp = TestIdentity(CordaX500Name("MegaCorp", "London", "GB"))
    private val miniCorp = TestIdentity(CordaX500Name("MiniCorp", "New York", "US"))
    private val price = 1
    private val asset = "Bond1"

    @Test
    fun `transaction must include Create command`() {
        ledgerServices.ledger {
            transaction {
                output(TradeContract.ID, TradeState(price, asset, miniCorp.party, megaCorp.party))
                fails()
                command(listOf(megaCorp.publicKey, miniCorp.publicKey), TradeContract.Commands.Create())
                verifies()
            }
        }
    }

    @Test
    fun `transaction must have no inputs`() {
        ledgerServices.ledger {
            transaction {
                input(TradeContract.ID, TradeState(price, asset, miniCorp.party, megaCorp.party))
                output(TradeContract.ID, TradeState(price, asset, miniCorp.party, megaCorp.party))
                command(listOf(megaCorp.publicKey, miniCorp.publicKey), TradeContract.Commands.Create())
                `fails with`("No inputs should be consumed when issuing a Trade.")
            }
        }
    }

    @Test
    fun `transaction must have one output`() {
        ledgerServices.ledger {
            transaction {
                output(TradeContract.ID, TradeState(price, asset, miniCorp.party, megaCorp.party))
                output(TradeContract.ID, TradeState(price, asset, miniCorp.party, megaCorp.party))
                command(listOf(megaCorp.publicKey, miniCorp.publicKey), TradeContract.Commands.Create())
                `fails with`("Only one output state should be created.")
            }
        }
    }

    @Test
    fun `buyer must sign transaction`() {
        ledgerServices.ledger {
            transaction {
                output(TradeContract.ID, TradeState(price, asset, miniCorp.party, megaCorp.party))
                command(miniCorp.publicKey, TradeContract.Commands.Create())
                `fails with`("All of the participants must be signers.")
            }
        }
    }

    @Test
    fun `seller must sign transaction`() {
        ledgerServices.ledger {
            transaction {
                output(TradeContract.ID, TradeState(price, asset, miniCorp.party, megaCorp.party))
                command(megaCorp.publicKey, TradeContract.Commands.Create())
                `fails with`("All of the participants must be signers.")
            }
        }
    }

    @Test
    fun `buyer is not seller`() {
        ledgerServices.ledger {
            transaction {
                output(TradeContract.ID, TradeState(price, asset, megaCorp.party, megaCorp.party))
                command(listOf(megaCorp.publicKey, miniCorp.publicKey), TradeContract.Commands.Create())
                `fails with`("The buyer and the seller cannot be the same entity.")
            }
        }
    }

    @Test
    fun `cannot create negative-price Trades`() {
        ledgerServices.ledger {
            transaction {
                output(TradeContract.ID, TradeState(-1, asset, miniCorp.party, megaCorp.party))
                command(listOf(megaCorp.publicKey, miniCorp.publicKey), TradeContract.Commands.Create())
                `fails with`("The Trade's price must be non-negative.")
            }
        }
    }
}