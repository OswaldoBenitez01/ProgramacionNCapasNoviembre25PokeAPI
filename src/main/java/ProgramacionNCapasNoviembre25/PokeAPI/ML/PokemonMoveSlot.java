package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonMoveSlot {
    
    @JsonProperty("move")
    private NamedResource move;
    
    @JsonProperty("version_group_details")
    private NamedResource[] versionGroupDetails;

    public NamedResource getMove() {
        return move;
    }

    public void setMove(NamedResource move) {
        this.move = move;
    }

    public NamedResource[] getVersionGroupDetails() {
        return versionGroupDetails;
    }

    public void setVersionGroupDetails(NamedResource[] versionGroupDetails) {
        this.versionGroupDetails = versionGroupDetails;
    }
    
    
}
