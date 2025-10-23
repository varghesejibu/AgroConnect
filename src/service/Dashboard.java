package service;

import model.*;
import java.util.List;

public class Dashboard {
    public void showFarmerStats(Farmer farmer) {
        System.out.println("Total Produce: " + farmer.getProduceList().size());
    }

    public void showBuyerRecommendations(Buyer buyer, List<Produce> availableProduce) {
        System.out.println("Recommended for " + buyer.getUsername() + ":");
        availableProduce.forEach(p -> System.out.println(p.getDetails()));
    }
}
