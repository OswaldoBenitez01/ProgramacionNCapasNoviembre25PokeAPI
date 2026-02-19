package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonHeldItemSlot {
    
    @JsonProperty("item")
    private NamedResource item; 
    
    @JsonProperty("version_details")
    private NamedResource[] versionDetails;
}
