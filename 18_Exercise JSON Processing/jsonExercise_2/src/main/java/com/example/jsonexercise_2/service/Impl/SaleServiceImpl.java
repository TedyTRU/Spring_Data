package com.example.jsonexercise_2.service.Impl;

import com.example.jsonexercise_2.model.dto.ex4.CarsDto;
import com.example.jsonexercise_2.model.dto.ex5.CustomerCarsDto;
import com.example.jsonexercise_2.model.dto.ex6.SalesDiscountDto;
import com.example.jsonexercise_2.model.entity.*;
import com.example.jsonexercise_2.repository.SaleRepository;
import com.example.jsonexercise_2.service.CarService;
import com.example.jsonexercise_2.service.CustomerService;
import com.example.jsonexercise_2.service.SaleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CarService carService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public SaleServiceImpl(SaleRepository saleRepository, CarService carService, CustomerService customerService, ModelMapper modelMapper) {
        this.saleRepository = saleRepository;
        this.carService = carService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedSales() {

//        if (saleRepository.count() > 0) {
//            return;
//        }

        BigDecimal[] discounts = new BigDecimal[]{
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(40),
                BigDecimal.valueOf(50)
        };

        long count = customerService.getCarsCount();

        for (long i = 1; i <= count; i++) {
            Sale sale = new Sale();
            Customer customer = customerService.getCustomerById(i);
            Car car = carService.getRandomCar();
            sale.setCustomer(customer);

            sale.setCar(car);
//            try {
//                sale.setCar(car);
//            } catch (Throwable e) {
//                i--;
//            }

            int random = ThreadLocalRandom.current().nextInt(0, discounts.length);
            BigDecimal discount = discounts[random];

            if (customer.getYoungDriver()) {
                discount = discount.add(BigDecimal.valueOf(5L));
            }

            sale.setDiscountPercentage(discount);

            saleRepository.save(sale);
        }

    }

    @Override
    public List<SalesDiscountDto> getAllSales() {

        return saleRepository.findAll().stream().map(sale -> {

            Car car = sale.getCar();
            SalesDiscountDto salesDiscountDto = modelMapper.map(sale, SalesDiscountDto.class);

            salesDiscountDto.setCar(modelMapper.map(car, CarsDto.class));

            List<Part> parts = car.getPartList();

            BigDecimal price = BigDecimal.valueOf(0);
            for (Part part : parts) {
                price = price.add(part.getPrice());
            }
            salesDiscountDto.setPrice(price);

            BigDecimal discountedPrice = price.multiply(BigDecimal.valueOf(1L).subtract(sale.getDiscountPercentage().divide(BigDecimal.valueOf(100L))));
            salesDiscountDto.setPriceWithDiscount(discountedPrice);

            return salesDiscountDto;

        }).toList();

    }

    @Override
    public List<CustomerCarsDto> getCustomerCars() {

        List<Long> ids = saleRepository.findGroupedByCustomer().stream().map(sale -> sale.getId()).toList();
        List<CustomerCarsDto> list = new ArrayList<>();

        for (Long id : ids) {
            Integer boughtCars = saleRepository.findAllByCustomer_Id(id).size();
            CustomerCarsDto customerCarsDto = new CustomerCarsDto();
            customerCarsDto.setBoughtCars(boughtCars);

            saleRepository.findAllByCustomer_Id(id)
                    .forEach(sale -> {
                        BigDecimal spentMoney = BigDecimal.valueOf(0L);
                        List<Part> parts = sale.getCar().getPartList();
                        for (Part part : parts) {
                            spentMoney = spentMoney.add(part.getPrice());
                        }
                        customerCarsDto.setSpentMoney(spentMoney);
                        customerCarsDto.setFullName(sale.getCustomer().getName());

                    });
            list.add(customerCarsDto);
        }

        return list;
    }

    @Override
    public List<Sale> getSalesByCustomer(Long id) {

        return saleRepository.findAllByCustomer_Id(id);

    }


}
