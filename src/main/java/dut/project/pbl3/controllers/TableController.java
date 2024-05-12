package dut.project.pbl3.controllers;

import com.google.gson.Gson;
import dut.project.pbl3.dto.table.CreateTableDto;
import dut.project.pbl3.dto.table.GetTableDto;
import dut.project.pbl3.dto.table.UpdateTableDto;
import dut.project.pbl3.services.TableService;
import dut.project.pbl3.utils.httpResponse.NoContentResponse;
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
@RequestMapping("tables")
public class TableController {

    private final TableService tableService;

    @SneakyThrows
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String createOne(@Valid @RequestBody CreateTableDto createTableDto) {
        this.tableService.createOne(createTableDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String updateOne(@Valid @RequestBody UpdateTableDto updateTableDto,
                            @PathVariable("id") Long id) {
        this.tableService.updateOne(id, updateTableDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String deleteOne(@PathVariable("id") Long id) {
        this.tableService.deleteOne(id);
        return new Gson().toJson(new NoContentResponse());
    }

    @GetMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<GetTableDto> getAllTables() {
        return tableService.findAll();
    }

    @GetMapping("/active")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<GetTableDto> getActiveTables() {
        return tableService.findAllActive();
    }
}
