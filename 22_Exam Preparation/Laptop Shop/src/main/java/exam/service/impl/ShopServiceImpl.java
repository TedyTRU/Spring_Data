package exam.service.impl;

import exam.model.dto.ShopSeedRootDto;
import exam.model.entity.Shop;
import exam.model.entity.Town;
import exam.repository.ShopRepository;
import exam.service.ShopService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ShopServiceImpl implements ShopService {

    public static final String SHOPS_FILE_PATH = "src/main/resources/files/xml/shops.xml";

    private final ShopRepository shopRepository;
    private final TownService townService;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public ShopServiceImpl(ShopRepository shopRepository, TownService townService, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.shopRepository = shopRepository;
        this.townService = townService;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOPS_FILE_PATH));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        ShopSeedRootDto shopSeedRootDto = xmlParser.fromFile(SHOPS_FILE_PATH, ShopSeedRootDto.class);

        shopSeedRootDto
                .getShops()
                .stream()
                .filter(shopSeedDto -> {
                    boolean isValid = validationUtil.isValid(shopSeedDto)
                            && !isEntityExist(shopSeedDto.getName());

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported Shop %s - %.0f", shopSeedDto.getName(), shopSeedDto.getIncome())
                                    : "Invalid shop")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(shopSeedDto -> {
                    Shop shop = modelMapper.map(shopSeedDto, Shop.class);
                    Town town = townService.findTownByName(shopSeedDto.getTown().getName());
                    shop.setTown(town);

                    return shop;
                })
                .forEach(shopRepository::save);

        return sb.toString();
    }

    @Override
    public boolean isEntityExist(String name) {
        return shopRepository.existsByName(name);
    }

    @Override
    public Shop findShopByName(String name) {
        return shopRepository.findShopByName(name).orElse(null);
    }
}
