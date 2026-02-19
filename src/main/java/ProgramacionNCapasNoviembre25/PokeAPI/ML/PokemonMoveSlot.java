package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonMoveSlot {
    
    @JsonProperty("move")
    private NamedResource move;
    
    @JsonProperty("version_group_details")
    private NamedResource[] versionGroupDetails;
}
