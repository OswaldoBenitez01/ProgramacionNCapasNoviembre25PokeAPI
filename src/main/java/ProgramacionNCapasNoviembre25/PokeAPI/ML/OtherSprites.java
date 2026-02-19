
package ProgramacionNCapasNoviembre25.PokeAPI.ML;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherSprites {

    @JsonProperty("official-artwork")
    private OfficialArtworkSprites officialArtwork;

    @JsonProperty("home")
    private HomeSprites home;

    @JsonProperty("dream_world")
    private DreamWorldSprites dreamWorld;

    @JsonProperty("showdown")
    private ShowdownSprites showdown;

    public OfficialArtworkSprites getOfficialArtwork() {
        return officialArtwork;
    }

    public void setOfficialArtwork(OfficialArtworkSprites officialArtwork) {
        this.officialArtwork = officialArtwork;
    }

    public HomeSprites getHome() {
        return home;
    }

    public void setHome(HomeSprites home) {
        this.home = home;
    }

    public DreamWorldSprites getDreamWorld() {
        return dreamWorld;
    }

    public void setDreamWorld(DreamWorldSprites dreamWorld) {
        this.dreamWorld = dreamWorld;
    }

    public ShowdownSprites getShowdown() {
        return showdown;
    }

    public void setShowdown(ShowdownSprites showdown) {
        this.showdown = showdown;
    }
    
    
}

