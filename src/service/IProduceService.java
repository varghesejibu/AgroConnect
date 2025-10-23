package service;

import model.Produce;
import java.util.List;

public interface IProduceService {
    void addProduce(String name, double price);
    List<Produce> getAllProduce();
}
