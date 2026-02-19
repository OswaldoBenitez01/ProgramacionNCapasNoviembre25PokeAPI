
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonAbility {
    @JsonProperty("is_hidden")
    private Boolean isHidden;   // elimínalo si no lo necesitas
    private Integer slot;
    private NamedResource ability;

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public NamedResource getAbility() {
        return ability;
    }

    public void setAbility(NamedResource ability) {
        this.ability = ability;
    }
    
    
}
