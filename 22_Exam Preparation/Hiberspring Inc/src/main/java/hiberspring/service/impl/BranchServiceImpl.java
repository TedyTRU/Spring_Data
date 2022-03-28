package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.BranchSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static hiberspring.common.Constants.*;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public BranchServiceImpl(BranchRepository branchRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.branchRepository = branchRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean branchesAreImported() {
        return branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return Files.readString(Path.of(BRANCHES_FILE_PATH));
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();

        BranchSeedDto[] branchSeedDtos = gson.fromJson(readBranchesJsonFile(), BranchSeedDto[].class);

        Arrays.stream(branchSeedDtos)
                .filter(branchSeedDto -> {
                    boolean isValid = validationUtil.isValid(branchSeedDto);

                    sb
                            .append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_MESSAGE, "Branch", branchSeedDto.getName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(branchSeedDto -> {
                    Branch branch = modelMapper.map(branchSeedDto, Branch.class);
                    Town town = townService.findByName(branchSeedDto.getTown());

                    branch.setTown(town);
                    return branch;
                })
                .forEach(branchRepository::save);

        return sb.toString();
    }

    @Override
    public Branch findByName(String name) {
        return branchRepository.findBranchByName(name).orElse(null);
    }

    @Override
    public boolean isEntityExistByName(String name) {
        return branchRepository.existsByName(name);
    }
}
