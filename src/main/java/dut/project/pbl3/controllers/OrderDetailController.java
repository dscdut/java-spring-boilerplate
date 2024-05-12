package dut.project.pbl3.controllers;

import com.google.gson.Gson;
import dut.project.pbl3.dto.orderDetail.delete.DeleteOrderDetailDto;
import dut.project.pbl3.dto.orderDetail.update.UpdatePriceDto;
import dut.project.pbl3.models.ApplicationUser;
import dut.project.pbl3.services.OrderDetailService;
import dut.project.pbl3.utils.httpResponse.NoContentResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("orderDetails")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @SneakyThrows
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @ResponseBody
    public String cancelItem(@Valid @RequestBody DeleteOrderDetailDto deleteContentDto,
                             @PathVariable("id") Long id, Authentication authentication) {
        Long userId = ((ApplicationUser) authentication.getPrincipal()).getUser().getId();
        this.orderDetailService.cancelItem(id, userId, deleteContentDto.getReason());
        return new Gson().toJson(new NoContentResponse());
    }

    @SneakyThrows
    @PutMapping("/{id}/update-price")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String updatePrice(@Valid @RequestBody UpdatePriceDto updatePriceDto,
                             @PathVariable("id") Long id) {
        this.orderDetailService.updatePrice(id, updatePriceDto);
        return new Gson().toJson(new NoContentResponse());
    }
}
