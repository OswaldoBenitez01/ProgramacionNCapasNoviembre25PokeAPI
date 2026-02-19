
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonType {
    private Integer slot;
    private NamedResource type;

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public NamedResource getType() {
        return type;
    }

    public void setType(NamedResource type) {
        this.type = type;
    }
    
    
}

