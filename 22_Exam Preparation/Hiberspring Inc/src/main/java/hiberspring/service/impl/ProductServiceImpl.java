package hiberspring.service.impl;

import hiberspring.domain.dtos.ProductDtos.ProductSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.BranchService;
import hiberspring.service.ProductService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static hiberspring.common.Constants.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BranchService branchService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public ProductServiceImpl(ProductRepository productRepository, BranchService branchService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.productRepository = productRepository;
        this.branchService = branchService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean productsAreImported() {
        return productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return Files.readString(Path.of(PRODUCTS_FILE_PATH));
    }

    @Override
    public String importProducts() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();

        ProductSeedRootDto productSeedRootDto = xmlParser.parseXml(ProductSeedRootDto.class, PRODUCTS_FILE_PATH);

        productSeedRootDto
                .getProducts()
                .stream()
                .filter(productSeedDto -> {
                    boolean isValid = validationUtil.isValid(productSeedDto)
                            && productSeedDto.getClients() != null
                            && !isEntityExistByName(productSeedDto.getName())
                            && branchService.isEntityExistByName(productSeedDto.getBranch());

                    sb
                            .append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_MESSAGE, "Product", productSeedDto.getName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    Branch branch = branchService.findByName(productSeedDto.getBranch());

                    product.setBranch(branch);
                    return product;
                })
                .forEach(productRepository::save);

        return sb.toString();
    }

    @Override
    public boolean isEntityExistByName(String name) {
        return productRepository.existsByName(name);
    }
}
