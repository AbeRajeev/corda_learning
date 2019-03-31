package java_bootcamp;

import java_examples.ArtContract;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireThat;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract{
    public static String ID = "java_bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getInputStates().size() != 0)
            throw new IllegalArgumentException("Transaction must have zero inputs");
        if (tx.getOutputStates().size() != 1)
            throw new IllegalArgumentException("Transactions must have one output");
        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException("Transactions must have one command");

        ContractState output = tx.getOutput(0);
        Command command = tx.getCommand(0);

        if(!(output instanceof TokenState ))
            throw new IllegalArgumentException("Output must be a token state");
        if(!(command.getValue() instanceof Commands.Issue))
            throw new IllegalArgumentException("Command must be issue command");

        TokenState token = (TokenState) output;
        if (token.getAmount() < 0)
            throw new IllegalArgumentException("Token amount must be positive");

        List<PublicKey> requiredSigner = command.getSigners();
        Party issuer = token.getIssuer();
        PublicKey issuerKey = issuer.getOwningKey();
        if (!(requiredSigner.contains(issuerKey)))
            throw new IllegalArgumentException("Issuer must be required signer");

    }

    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}