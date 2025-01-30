package main.model.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.Element;

@Getter
@Setter(AccessLevel.PROTECTED)
public class MonsterCard extends Card {

    @JsonCreator
    public MonsterCard(@JsonProperty("Id") String id, @JsonProperty("Name") String name, @JsonProperty("Damage") int damage) {
        super(name,damage,id);
        setMonsterCardWeakness();
    }


    public MonsterCard(Element type, String name, int damage, String weakness, Element typeWeakness, String id,
                       String nameAndType, String packageId, String userId) {
        super(type, name, damage, weakness, typeWeakness, id, nameAndType, packageId, userId);
    }


    private void setMonsterCardWeakness() {
        if (name.contains("Goblin")) {
            setWeakness("Dragon");
        } else if (name.contains("Knight")) {
            setWeakness("WaterSpell");
        } else if (name.contains("Dragon")) {
            setWeakness("FireElve");
        } else if (name.contains("Ork")) {
            setWeakness("Wizard");
        }
    }
}
