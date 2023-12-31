package com.example.springbootnodb.service;

import com.example.springbootnodb.model.Car;
import com.example.springbootnodb.model.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final OrderService orderService;
    private final List<Car> carList = new ArrayList<>();

    public List<Car> getCarList() {
        return carList.stream().filter(car -> !car.isDeleted()).collect(Collectors.toList());
    }

    public void saveCar(Car car) {
        car.setId((long) carList.size());
        car.setDeleted(false);
        carList.add(car);
    }

    public void deleteCar(Long carId) {
        Car selectedCar = null;
        for (Car carItr : carList) {
            if (carItr.getId().equals(carId)) {
                selectedCar = carItr;
                break;
            }
        }

        if (selectedCar == null) return;

        selectedCar.setDeleted(true);
    }

    public void updateCar(Car car) {
        Car selectedCar = null;
        for (Car carItr : carList) {
            if (carItr.getId().equals(car.getId())) {
                selectedCar = carItr;
                break;
            }
        }

        if (selectedCar == null) return;

        if (car.getStock() != null) {
            selectedCar.setStock(car.getStock());
        }
        if (car.getPrice() != null) {
            selectedCar.setPrice(car.getPrice());
        }
    }

    public void buyCar(Long carToBuyId, Long quantity) {
        Car selectedCar = null;
        for (Car carItr : carList) {
            if (carItr.getId().equals(carToBuyId)) {
                selectedCar = carItr;
                break;
            }
        }

        if (selectedCar == null || selectedCar.getStock() < quantity) return;

        selectedCar.setStock(selectedCar.getStock() - quantity);

        orderService.saveOrder(new Order((long) orderService.getOrderList().size(), carToBuyId, quantity, quantity * selectedCar.getPrice()));
    }
}
