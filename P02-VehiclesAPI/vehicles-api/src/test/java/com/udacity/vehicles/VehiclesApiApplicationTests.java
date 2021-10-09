package com.udacity.vehicles;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class VehiclesApiApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test_get_vehicles() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("http://localhost:" + port + "/services/price?vehicleId=1", String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void test_create_car() throws Exception {
        Car car = initCarModel();
        mvc.perform(post(new URI("/cars"))
                                .content(json.write(car).getJson())
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

    }

    @Test
    public void test_update_car() throws Exception{
        Car car = initCarModel();
        car.setPrice("200,000 SR");
        car.setCreatedAt( LocalDateTime.of(2020, Month.JANUARY,1,0,0));
        mvc.perform(put(new URI("/cars/1"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(HttpStatus.OK.value()));

    }

    @Test
    public void test_delete_car() throws Exception{
        Car car = initCarModel();
        mvc.perform(delete("/cars/"+car.getId())).
                andExpect(status().is(HttpStatus.OK.value()));
    }


    @Test
    public void test_get_car_list() throws Exception {
        mvc.perform(get("/cars/")).andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{}"));
        verify(carService, times(1)).list();

    }

    @Test
    public void test_find_car() throws Exception{
        Car car = initCarModel();
        mvc.perform(get("/cars/"+car.getId())).
                andExpect(status().isOk())
                .andExpect(content().json("{}"));

    }



    private Car initCarModel() {
        Car car = new Car();
        car.setLocation(new Location(32.7936352, -116.9353021));
        car.setPrice("150,000 SR");
        car.setCreatedAt( LocalDateTime.of(2019, Month.JANUARY,1,0,0));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(102, "Ford");
        details.setManufacturer(manufacturer);
        details.setModel("Ford");
        details.setExternalColor("white");
        details.setBody("suv");
        details.setFuelType("Gasoline");
        details.setEngine("3.3-liter V-6");
        details.setMileage(100);
        details.setModelYear(2021);
        details.setProductionYear(2020);
        details.setNumberOfDoors(5);
        car.setDetails(details);
        car.setCondition(Condition.NEW);
        return car;
    }

}
