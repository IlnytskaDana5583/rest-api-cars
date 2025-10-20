package de.ait.training.controller;

import de.ait.training.model.Car;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Cars", description = "Operation on cars")
@Slf4j
@RequestMapping("/api/cars")
@RestController
public class RestApiCarController {

    Car carOne = new Car(1, "black", "BMW x5", 25000);
    Car carTwo = new Car(2, "green", "Audio", 15000);
    Car carThree = new Car(3, "white", "MB ", 18000);
    Car carFour = new Car(4, "red", "Ferrari", 27000);

    List<Car> cars = new ArrayList<>();

    public RestApiCarController() {
        cars.add(carOne);
        cars.add(carTwo);
        cars.add(carThree);
        cars.add(carFour);
    }

    //GET/api/cars
    // @return вовращает весь список всех автомобилей
    // GET --> api/cars
    @GetMapping()
    Iterable<Car> getCars() {//интерфейс
        return cars;
    }


    /**
     * Создает новый автомобиль и добавляет его в лист
     *
     * @param car
     * @return созданный автомобиль
     */
    @Operation(
            summary = "Create car",
            description = "Create new car",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Created")
            }
    )
    @PostMapping()
    Car postCar(@RequestBody Car car) {
        if (car.getId() <= 0) {
            log.error("car id is invalid");
            Car errorCar = new Car(9999, "000", "000", 999);
            return errorCar;
        }
        cars.add(car);
        return car;
    }

    //замена существующего авто,если id

    @PutMapping("/{id}")
    ResponseEntity<Car> putCar(@PathVariable long id, @RequestBody Car car) {
        int carIndex = -1;
        for (Car carInList : cars) {
            if (carInList.getId() == id) {
                carIndex = cars.indexOf(carInList);
                cars.set(carIndex, car);
            }
        }

        return (carIndex == -1)
                ? new ResponseEntity<>(postCar(car), HttpStatus.CREATED)
                : new ResponseEntity<>(car, HttpStatus.OK);
    }

    /**
     * удаляем автомобиль по id
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    void deleteCar(@PathVariable long id) {
        log.info("Delete car with id {}", id);
        cars.removeIf(car -> car.getId() == id);
    }

    /**
     * GET /api/cars/color/{color}
     * Возвращает список всех автомобилей заданного цвета
     */
    @Operation(
            summary = "Get cars by color",
            description = "Returns a list of cars filtered by color",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cars found or empty list")
            }
    )

    @GetMapping("/color/{color}")
    public Object getCarsByColor(@PathVariable String color) {
        log.info("Searching cars by color: {}", color);

        List<Car> filteredCars = cars.stream()
                .filter(car -> car.getColor().equalsIgnoreCase(color))
                .toList();

        if (filteredCars.isEmpty()) {
            log.warn("No cars found for color {}", color);
            // Повертаємо повідомлення, якщо машин не знайдено
            return "No cars found for color: " + color;
        }

        log.info("Found {} cars with color {}", filteredCars.size(), color);
        return filteredCars;
    }


    }
