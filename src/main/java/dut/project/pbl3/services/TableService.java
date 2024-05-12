package dut.project.pbl3.services;

import dut.project.pbl3.dto.table.CreateTableDto;
import dut.project.pbl3.dto.table.GetTableDto;
import dut.project.pbl3.dto.table.UpdateTableDto;
import dut.project.pbl3.models.Table;
import dut.project.pbl3.models.TableType;
import dut.project.pbl3.repositories.TableRepository;
import dut.project.pbl3.repositories.TableTypeRepository;
import dut.project.pbl3.utils.ObjectMapperUtils;
import dut.project.pbl3.utils.httpResponse.exceptions.BadRequestException;
import dut.project.pbl3.utils.httpResponse.exceptions.DuplicateException;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TableService {

    private final TableRepository tableRepository;
    private final TableTypeRepository tableTypeRepository;

    public void createOne(CreateTableDto createTableDto) throws BadRequestException, NotFoundException {
        Optional<Table> foundTableByName = this.tableRepository.findByNameEquals(createTableDto.getName());
        if (foundTableByName.isPresent())
            throw new BadRequestException("Table Name is existed");

        Optional<TableType> foundTableType = tableTypeRepository.findById(createTableDto.getIdTableType());
        foundTableType.orElseThrow(() -> new NotFoundException("Table type Id is invalid"));

        Table table = new Table();

        table.setName(createTableDto.getName());
        table.setTableType(foundTableType.get());

        this.tableRepository.save(table);
    }

    public void updateOne(Long id, UpdateTableDto updateTableDto) throws NotFoundException, DuplicateException {
        Optional<Table> foundTable = this.tableRepository.findById(id);
        foundTable.orElseThrow(() -> new NotFoundException("Not found Table"));


        if (!updateTableDto.getName().equals(foundTable.get().getName())) {
            Optional<Table> foundTableByName = this.tableRepository.findByNameEquals(updateTableDto.getName());
            if (foundTableByName.isPresent() && foundTableByName.get().getId() != id)
                throw new DuplicateException("Name is used");
        }

        Optional<TableType> foundTableType = this.tableTypeRepository.findById(updateTableDto.getIdTableType());
        foundTableType.orElseThrow(() -> new NotFoundException("Not found Table Type"));

        if (Boolean.TRUE.equals(updateTableDto.getIsRestore()))
            this.tableRepository.restoreTable(id);
        foundTable.get().setTableType(foundTableType.get());
        foundTable.get().setName(updateTableDto.getName());

        this.tableRepository.save(foundTable.get());
    }

    public void deleteOne(Long id) throws NotFoundException {
        Optional<Table> table = this.tableRepository.findById(id);
        table.orElseThrow(() -> new NotFoundException("Not found Table"));

        try {
            this.tableRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (table.get().getDeletedAt() == null) {
                table.get().setDeletedAt(timestamp);
                this.tableRepository.save(table.get());
            }
        }
    }

    public List<GetTableDto> findAll() {
        return ObjectMapperUtils.mapAll(tableRepository.findAll(), GetTableDto.class);
    }

    public List<GetTableDto> findAllActive() {
        return ObjectMapperUtils.mapAll(tableRepository.findAllByDeletedAtIsNull(), GetTableDto.class);
    }
}
