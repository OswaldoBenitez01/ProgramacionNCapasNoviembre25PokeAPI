package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonHeldItemSlot {
    
    @JsonProperty("item")
    private NamedResource item; 
    
    @JsonProperty("version_details")
    private NamedResource[] versionDetails;

    public NamedResource getItem() {
        return item;
    }

    public void setItem(NamedResource item) {
        this.item = item;
    }

    public NamedResource[] getVersionDetails() {
        return versionDetails;
    }

    public void setVersionDetails(NamedResource[] versionDetails) {
        this.versionDetails = versionDetails;
    }
    
    
}
