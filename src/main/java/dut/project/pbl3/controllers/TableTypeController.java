package dut.project.pbl3.controllers;

import com.google.gson.Gson;
import dut.project.pbl3.dto.table.GetTableDto;
import dut.project.pbl3.dto.tableType.CreateTableTypeDto;
import dut.project.pbl3.dto.tableType.GetTableTypeDto;
import dut.project.pbl3.dto.tableType.UpdateTableTypeDto;
import dut.project.pbl3.services.TableTypeService;
import dut.project.pbl3.utils.httpResponse.NoContentResponse;
import dut.project.pbl3.utils.httpResponse.okResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("tableTypes")
public class TableTypeController {
    private final TableTypeService tableTypeService;

    @SneakyThrows
    @GetMapping("/{typeId}/tables")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public List<GetTableDto> findAll(@PathVariable("typeId") Long typeId) {
        return this.tableTypeService.findTables(typeId);
    }

    @SneakyThrows
    @GetMapping("/{typeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String findOne(@PathVariable("typeId") Long id) {
        GetTableTypeDto getTableTypeDto = this.tableTypeService.findById(id);
        return new Gson().toJson(new okResponse(getTableTypeDto));
    }

    @SneakyThrows
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String insertOne(@Valid @RequestBody CreateTableTypeDto createTableTypeDto) {
        GetTableTypeDto getTableTypeDto = this.tableTypeService.createOne(createTableTypeDto);
        return new Gson().toJson(new okResponse(getTableTypeDto));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String updateOne(@Valid @RequestBody UpdateTableTypeDto updateTableTypeDto,
                            @PathVariable Long id) {
        this.tableTypeService.updateOne(id, updateTableTypeDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String deleteOne(@PathVariable Long id) {
        this.tableTypeService.deleteOne(id);
        return new Gson().toJson(new NoContentResponse());
    }
}
