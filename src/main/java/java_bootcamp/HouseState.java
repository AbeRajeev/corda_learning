package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HouseState implements ContractState {

    private String address;
    private Party owner;

    public HouseState(String address, Party owner) {
        this.address = address;
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public Party getOwner() {
        return owner;
    }

    public static void main(String[] args) {
        Party Abe = null;
        HouseState state = new HouseState( "1700 Montgomery St, San Francisco", Abe);
    }


    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner); // this is not a very useful representation of house
    }
}
