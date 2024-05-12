package dut.project.pbl3.controllers;

import dut.project.pbl3.common.enums.RoleEnum;
import dut.project.pbl3.services.*;
import dut.project.pbl3.utils.HelperUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping()
public class AdminController {

    private final TableService tableService;
    private final TableTypeService tableTypeService;
    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final TableOrderService tableOrderService;

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getAdminView(Model model, Authentication authentication) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        model.addAttribute("email", authentication.getName());
        model.addAttribute("tableTypes", this.tableTypeService.findAll());
        model.addAttribute("tables", this.tableService.findAll());
        model.addAttribute("users", this.userService.findAll());
        model.addAttribute("roles", HelperUtil.getEnumNames(RoleEnum.class));
        model.addAttribute("products", this.productService.getAllProducts());
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("bills", this.tableOrderService.findFinished());
        model.addAttribute("totalAmount",
                numberFormat.format(this.tableOrderService.getStatistic().getTotalAmount()));
        model.addAttribute("numberOfOrder",
                numberFormat.format(this.tableOrderService.getStatistic().getNumberOfOrder()));
        return "admin";
    }

    @GetMapping("/forbidden")
    public String forbiddenView() {
        return "forbidden";
    }
}
