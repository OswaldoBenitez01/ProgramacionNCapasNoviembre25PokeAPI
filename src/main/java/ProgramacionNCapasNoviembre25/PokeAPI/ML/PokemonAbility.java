
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonAbility {
    @JsonProperty("is_hidden")
    private Boolean hidden;
    private Integer slot;
    private NamedResource ability;

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
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
