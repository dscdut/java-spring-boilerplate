package dut.project.pbl3.services;

import dut.project.pbl3.dto.table.GetTableDto;
import dut.project.pbl3.dto.tableType.CreateTableTypeDto;
import dut.project.pbl3.dto.tableType.GetTableTypeDto;
import dut.project.pbl3.dto.tableType.UpdateTableTypeDto;
import dut.project.pbl3.models.TableType;
import dut.project.pbl3.repositories.TableTypeRepository;
import dut.project.pbl3.utils.ObjectMapperUtils;
import dut.project.pbl3.utils.httpResponse.exceptions.BadRequestException;
import dut.project.pbl3.utils.httpResponse.exceptions.DuplicateException;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TableTypeService {

    private final TableTypeRepository tableTypeRepository;

    public GetTableTypeDto createOne(CreateTableTypeDto createTableTypeDto) throws DuplicateException, BadRequestException {
        Optional<TableType> foundTableType = this.tableTypeRepository.findByNameEquals(createTableTypeDto.getName());
        if (foundTableType.isPresent())
            throw new DuplicateException("Table Type is existed ");

        TableType tableType = ObjectMapperUtils.map(createTableTypeDto, TableType.class);
        TableType createdOne = tableTypeRepository.save(tableType);
        return ObjectMapperUtils.map(createdOne, GetTableTypeDto.class);
    }

    public void updateOne(Long id, UpdateTableTypeDto updateTableTypeDto) throws DuplicateException, NotFoundException {
        Optional<TableType> foundTableType = this.tableTypeRepository.findById(id);
        foundTableType.orElseThrow(NotFoundException::new);

        Optional<TableType> foundTableTypeByName = this.tableTypeRepository.findByNameEquals(updateTableTypeDto.getName());
        if (foundTableTypeByName.isPresent() && foundTableTypeByName.get().getId() != id)
            throw new DuplicateException("This name is used");

        foundTableType.get().setName(updateTableTypeDto.getName());
        foundTableType.get().setPrice(updateTableTypeDto.getPrice());

        this.tableTypeRepository.save(foundTableType.get());
    }

    public void deleteOne(Long id) {
        this.tableTypeRepository.deleteById(id);
    }

    public List<GetTableTypeDto> findAll() {
        return ObjectMapperUtils.mapAll(tableTypeRepository.findAll(), GetTableTypeDto.class);
    }

    public GetTableTypeDto findById(Long id) throws NotFoundException {
        Optional<TableType> tableType = this.tableTypeRepository.findById(id);
        tableType.orElseThrow(NotFoundException::new);
        return ObjectMapperUtils.map(tableType, GetTableTypeDto.class);
    }

    public List<GetTableDto> findTables(Long id) throws NotFoundException {
        Optional<TableType> tableType = this.tableTypeRepository.findById(id);
        tableType.orElseThrow(() -> new NotFoundException("Not found Table Type Id"));
        return ObjectMapperUtils.mapAll(tableType.get().getTables(), GetTableDto.class);
    }
}
