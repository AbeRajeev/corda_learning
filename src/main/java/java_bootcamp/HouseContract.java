package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class HouseContract implements Contract {

    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getCommands().size() != 1)
            throw new IllegalArgumentException("Transactions must have one command");
        Command command = tx.getCommand(0);
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandType = command.getValue();

        if (commandType instanceof Register){
            // registration transaction logic
            // shape constraints
            if (tx.getInputStates().size() != 0)
                throw new IllegalArgumentException("Registration transaction must have no inputs");
            // each transaction should register one house
            if (tx.getInputStates().size() != 1)
                throw new IllegalArgumentException("Each transaction can register one house at a time");

            // content constraints
            ContractState outputState = tx.getOutput(0);
            if (!(outputState instanceof HouseState))
                throw new IllegalArgumentException("output must be a house state");
            HouseState houseState = (HouseState) outputState;
            if (houseState.getAddress().length() <= 3)
                throw new IllegalArgumentException("Address must be more than 3 characters");

            // required signer constraints
            Party owner = houseState.getOwner();
            PublicKey ownersKey = owner.getOwningKey();
            if (!(requiredSigners.contains(ownersKey)))
                throw new IllegalArgumentException("You are not an authorized person.");

        } else if (commandType instanceof Transfer){
            // transfer logic goes in here
            // shape constraints
            if (tx.getInputStates().size() != 1)
                throw new IllegalArgumentException("must have one input");
            if (tx.getOutputStates().size() != 1)
                throw new IllegalArgumentException("must have one output");

            // content constraints
            ContractState input = tx.getInput(0);
            ContractState output = tx.getOutput(0);

            if(!(input instanceof HouseState))
                throw new IllegalArgumentException("Input must be a HouseState");
            if(!(input instanceof HouseState))
                throw new IllegalArgumentException("Input must be a HouseState");

            HouseState inputHouse = (HouseState) input;
            HouseState outputHouse = (HouseState) output;

            if (!(inputHouse.getAddress().equals(outputHouse.getAddress())))
                throw new IllegalArgumentException("The address of houses mismatch, address cant change in a tx");
            if (inputHouse.getOwner().equals(outputHouse.getOwner()))
                throw new IllegalArgumentException("The owner must change in a tx.");

            // signer constraints
            Party inputOwner = inputHouse.getOwner();
            Party outputOwner = outputHouse.getOwner();

            if (!(requiredSigners.contains(inputOwner.getOwningKey())))
                throw new IllegalArgumentException("Current owner must sign the transaction");
            if (!(requiredSigners.contains(outputOwner.getOwningKey())))
                throw new IllegalArgumentException("New owner must sign the transaction");


        } else {
            throw new IllegalArgumentException("command type not recognized");
        }

    }

    public class Register implements CommandData {}
    public class Transfer implements CommandData {}


}
