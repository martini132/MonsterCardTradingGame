package main.model.card;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.Element;

import java.util.*;

@Getter
@Setter(AccessLevel.PROTECTED)

public abstract class Card {

    @JsonAlias({"Type"})
    protected Element type;
    @JsonAlias({"Name"})
    protected String name;
    @JsonAlias({"Damage"})
    protected int damage;
    @JsonAlias({"Weakness"})
    private String weakness;
    @JsonAlias({"TypeWeakness"})
    private Element typeWeakness; //Weakness against an element
    @JsonAlias({"Id"})
    private String id;
    @JsonAlias({"NameAndType"})
    private String nameAndType;
    @JsonAlias({"packageId"})
    private String packageId;
    @JsonAlias({"userId"})
    private String userId;


    public Card(String name, int damage, String id) {

        setId(id);
        setName(name);
        setDamage(damage);
        setNameAndType(name.toLowerCase(Locale.ROOT));

        if (name.contains("Water")) {
            setType(Element.WATER);
            setTypeWeakness(Element.NORMAL);
        } else if (name.contains("Fire")) {
            setType(Element.FIRE);
            setTypeWeakness(Element.WATER);
        } else {
            setType(Element.NORMAL);
            setTypeWeakness(Element.FIRE);
            setNameAndType((type + "" + name).toLowerCase(Locale.ROOT));
        }
    }

    public Card(Element type, String name, int damage, String weakness,
                Element typeWeakness, String id, String nameAndType, String packageId, String userId) {
        this.type = type;
        this.name = name;
        this.damage = damage;
        this.weakness = weakness;
        this.typeWeakness = typeWeakness;
        this.id = id;
        this.nameAndType = nameAndType;
        this.packageId = packageId;
        this.userId = userId;
    }


    public void changePackageId(String packageId) {
        setPackageId(packageId);
    }

    public void changeUserId(String userId) {
        setUserId(userId);
    }

    @Override
    public String toString() {
        return "Card{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", damage=" + damage +
                ", typeWeakness=" + typeWeakness +
                ", id='" + id + '\'' +
                ", nameAndType='" + nameAndType + '\'' +
                ", packageid='" + packageId + '\'' +
                ", userID='" + userId + '\'' +
                '}';
    }
}
