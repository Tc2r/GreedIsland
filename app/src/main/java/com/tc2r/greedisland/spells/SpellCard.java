package com.tc2r.greedisland.spells;

/**
 * Created by Tc2r on 2/25/2017.
 * <p>
 * Description:
 */

public class SpellCard {

    private int id, cardNumber, limit;
    private String name, description, rank, image;

    public SpellCard() {
    }

    public SpellCard(int id, int cardNumber, String name, String rank, int limit, String image, String description) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.limit = limit;
        this.name = name;
        this.description = description;
        this.rank = rank;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
