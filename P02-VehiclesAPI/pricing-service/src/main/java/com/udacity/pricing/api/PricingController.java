package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implements a REST-based controller for the pricing service.
 */
@RestController
@RequestMapping("/services/price")
public class PricingController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webclientBuilder;



    /**
     * Gets the price for a requested vehicle.
     * @param vehicleId ID number of the vehicle for which the price is requested
     * @return price of the vehicle, or error that it was not found.
     */
//    @GetMapping
//    public Price get(@RequestParam Long vehicleId) {
//        try {
//            return PricingService.getPrice(vehicleId);
//        } catch (PriceException ex) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, "Price Not Found", ex);
//        }
//
//    }

    @GetMapping("/{vehicleId}")
    public Price get(@PathVariable(name = "vehicleId") Long vehicleId){
        return restTemplate.getForObject("http://pricing-service/"+ vehicleId, Price.class);
    }
}
