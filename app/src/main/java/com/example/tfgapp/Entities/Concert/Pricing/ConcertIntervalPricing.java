package com.example.tfgapp.Entities.Concert.Pricing;


public class ConcertIntervalPricing {

    private String id;
    private String name;
    private String description;
    private int numberTickets;
    private double cost;
    private double discountApplied;
    private String concertId;

    public ConcertIntervalPricing(){}

    public ConcertIntervalPricing(String id, String name, String description, int numberTickets, double cost, double discountApplied) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberTickets = numberTickets;
        this.cost = cost;
        this.discountApplied = discountApplied;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getNumberTickets() {
        return numberTickets;
    }

    public void setNumberTickets(int numberTickets) {
        this.numberTickets = numberTickets;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(double discountApplied) {
        this.discountApplied = discountApplied;
    }

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    @Override
    public String toString() {
        return "ConcertIntervalPricing{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", numberTickets=" + numberTickets +
                ", cost=" + cost +
                ", discountApplied=" + discountApplied +
                ", concertId='" + concertId + '\'' +
                '}';
    }
}
