package de.ait.training.controller;

import de.ait.training.model.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.contains;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RestApiCarControllerIT {


    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @DisplayName("price between 10000 and 30000, 3 cars were found, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testPriceBetween10000And30000() throws Exception {
        ResponseEntity<Car[]> response = restTemplate.getForEntity(url("/api/cars/price/between/10000/30000"),
                Car[].class);
        //assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Car> cars = Arrays.asList(response.getBody());
        assertThat(cars.size()).isEqualTo(3);
        assertThat(cars.get(0).getModel()).isEqualTo("BMW x5");

    }

    @Test
    @DisplayName("price under 16000, 1 car was found, status OK")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testPriceUnder16000Success() {
        ResponseEntity<Car[]> response = restTemplate.getForEntity(url("/api/cars/price/under/16000"),
                Car[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Car> cars = Arrays.asList(response.getBody());
        assertThat(cars.size()).isEqualTo(1);
        assertThat(cars.get(0).getModel()).isEqualTo("Audi A4");
    }

    @Test
    @DisplayName("wrong min and max price, 0 cars ware found, status BadRequest")
    void testMinMaxPricesWrongFail() {
        ResponseEntity<Car[]> response = restTemplate.getForEntity(url("/api/cars/price/between/30000/10000"),
                Car[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Car[] result = response.getBody();
        List<Car> cars = Arrays.asList(response.getBody());
        assertThat(cars.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("price between 100 and 500, no cars found, status NOT_FOUND")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testPriceBetween100And500NotFound () {
        ResponseEntity<Car[]> response = restTemplate.getForEntity(url("/api/cars/price/between/100/500"),
                Car[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEmpty();

    }

    @Test
    @DisplayName("price over 1000000, no cars found, status NOT_FOUND")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void testPriceOver1000000NotFound () {

        ResponseEntity<Car[]> response = restTemplate.getForEntity(
                url("/api/cars/price/over/1000000"),
                Car[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("should return 404 and empty array when color not found\"")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void shouldReturn404WhenColorNotFound() {
        ResponseEntity<Car[]> response = restTemplate.getForEntity(url("/api/cars/color/purple"),
                Car[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }


    @Test
    @DisplayName("Should return cars when model is found\"")
    @Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
    void shouldReturnCarsWhenModelFound() {
        ResponseEntity<Car[]> response = restTemplate.getForEntity(url("/api/cars/model/MB A220"),
                Car[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.asList(response.getBody())
                .get(0)
                .getModel())
                .isEqualToIgnoringCase("MB A220");
        List<Car> cars = Arrays.asList(response.getBody());
    }


@Test
@DisplayName("should return 404 when no cars found under price 1000")
@Sql(scripts = {"classpath:sql/clear.sql", "classpath:sql/seed_cars.sql"})
void shouldReturn404WhenNoCarsBelowPrice1000() {

    ResponseEntity<Car[]> response = restTemplate.getForEntity(
            url("/api/cars/price/under/1000"), Car[].class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isEmpty();

}



    }





































