package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.LaptopSeedDto;
import exam.model.entity.Laptop;
import exam.model.entity.Shop;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class LaptopServiceImpl implements LaptopService {

    public static final String LAPTOPS_FILE_PATH = "src/main/resources/files/json/laptops.json";

    private final LaptopRepository laptopRepository;
    private final ShopService shopService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopService shopService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.laptopRepository = laptopRepository;
        this.shopService = shopService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOPS_FILE_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder sb = new StringBuilder();

        LaptopSeedDto[] laptopSeedDtos = gson.fromJson(readLaptopsFileContent(), LaptopSeedDto[].class);

        Arrays.stream(laptopSeedDtos)
                .filter(laptopSeedDto -> {
                    boolean isValid = validationUtil.isValid(laptopSeedDto)
                            && !laptopRepository.existsByMacAddress(laptopSeedDto.getMacAddress());

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                                    laptopSeedDto.getMacAddress(), laptopSeedDto.getCpuSpeed(), laptopSeedDto.getRam(), laptopSeedDto.getStorage())
                                    : "Invalid Laptop")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(laptopSeedDto -> {
                    Laptop laptop = modelMapper.map(laptopSeedDto, Laptop.class);
                    Shop shop = shopService.findShopByName(laptopSeedDto.getShop().getName());
                    laptop.setShop(shop);
                    return laptop;
                })
                .forEach(laptopRepository::save);

        return sb.toString();
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder sb = new StringBuilder();

        List<Laptop> laptops = laptopRepository
                .findAllOrderByCpuDescThenByRamDescThenByStorageDescThenByMacAddress();

        laptops
                .forEach(laptop -> {
                    sb
                            .append(String.format("Laptop - %s", laptop.getMacAddress()))
                            .append(System.lineSeparator())
                            .append(String.format("*Cpu speed - %.2f", laptop.getCpuSpeed()))
                            .append(System.lineSeparator())
                            .append(String.format("**Ram - %d", laptop.getRam()))
                            .append(System.lineSeparator())
                            .append(String.format("***Storage - %d", laptop.getStorage()))
                            .append(System.lineSeparator())
                            .append(String.format("****Price - %.2f", laptop.getPrice()))
                            .append(System.lineSeparator())
                            .append(String.format("#Shop name - %s", laptop.getShop().getName()))
                            .append(System.lineSeparator())
                            .append(String.format("##Town - %s", laptop.getShop().getTown().getName()))
                            .append(System.lineSeparator())
                            .append(System.lineSeparator());

                });

        return sb.toString();
    }
}
