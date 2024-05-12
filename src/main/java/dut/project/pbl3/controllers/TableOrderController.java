package dut.project.pbl3.controllers;

import com.google.gson.Gson;
import dut.project.pbl3.dto.orderDetail.AddItemDto;
import dut.project.pbl3.dto.orderDetail.delete.DeleteOrderDetailDto;
import dut.project.pbl3.dto.orderDetail.update.UpdateItemDto;
import dut.project.pbl3.dto.tableOrder.*;
import dut.project.pbl3.dto.tableOrder.splitBill.SplitDto;
import dut.project.pbl3.models.ApplicationUser;
import dut.project.pbl3.services.TableOrderService;
import dut.project.pbl3.utils.httpResponse.NoContentResponse;
import dut.project.pbl3.utils.httpResponse.exceptions.UnauthorizedException;
import dut.project.pbl3.utils.httpResponse.okResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("tableOrders")
public class TableOrderController {
    private final TableOrderService tableOrderService;

    @SneakyThrows
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String findById(@PathVariable("id") Long id) {
        GetTableOrderDto getOrderDetailDto = this.tableOrderService.findActiveById(id);
        return new Gson().toJson(getOrderDetailDto);
    }

    @SneakyThrows
    @PostMapping("/{tableId}/order")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String orderTable(@PathVariable("tableId") Long tableId) {
        this.tableOrderService.createOrder(tableId);
        return new Gson().toJson(new NoContentResponse());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public List<GetTableOrderDto> findActives() {
        return this.tableOrderService.findActives();
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public List<GetTableOrderDto> findFinished() {
        return this.tableOrderService.findFinished();
    }

    @SneakyThrows
    @PostMapping("/{id}/stopTime")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String stopTime(@PathVariable("id") Long id) {
        String data = String.valueOf(this.tableOrderService.stopTime(id));
        return new Gson().toJson(new okResponse(data));
    }

    @SneakyThrows
    @PostMapping("/{id}/continueTime")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STAFF')")
    @ResponseBody
    public String continueTime(@PathVariable("id") Long id) {
        this.tableOrderService.continueTime(id);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String addBillItem(@RequestBody AddItemDto addItemDto) {
        this.tableOrderService.addItem(addItemDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String updateBillItem(@Valid @RequestBody UpdateItemDto updateItemDto,
                                 @PathVariable("id") Long id) {
        this.tableOrderService.updateItem(id, updateItemDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}/time-price")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String updateTimePrice(@Valid @RequestBody UpdateTimePriceDto updateTimePriceDto,
                                  @PathVariable("id") Long id) {
        this.tableOrderService.updateTimePrice(id, updateTimePriceDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String cancelBill(@Valid @RequestBody DeleteOrderDetailDto deleteOrderDetailDto,
                             @PathVariable("id") Long id,  Authentication authentication) {
        if (authentication == null) {
            throw new UnauthorizedException("you must login");
        }
        Long userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        this.tableOrderService.cancelOne(id, userId, deleteOrderDetailDto.getReason());
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/merge")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String mergeBills(@Valid @RequestBody MergeDto mergeDto) {
        this.tableOrderService.mergeBills(mergeDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String transferBill(@Valid @RequestBody TransferDto transferDto) {
        this.tableOrderService.transferBill(transferDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/split")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String splitBill(@Valid @RequestBody SplitDto splitDto){
        this.tableOrderService.splitBill(splitDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String payBill(@Valid @RequestBody PayTableOrderDto payTableOrderDto,
                          @PathVariable Long id) {
        this.tableOrderService.payBill(id, payTableOrderDto);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String deleteOne(@PathVariable("id") Long id) {
        this.tableOrderService.deleteOne(id);
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PostMapping("/{tableId}/restore")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String restoreOne(@PathVariable("tableId") Long id) {
        this.tableOrderService.restoreOne(id);
        return new Gson().toJson(new NoContentResponse());
    }
}
