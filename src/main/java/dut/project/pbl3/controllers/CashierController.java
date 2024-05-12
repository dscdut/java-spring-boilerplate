package dut.project.pbl3.controllers;

import dut.project.pbl3.services.CategoryService;
import dut.project.pbl3.services.TableTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class CashierController {

    private final TableTypeService tableTypeService;
    private final CategoryService categoryService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public String homepage() {
        return "redirect:cashier";
    }

    @GetMapping("cashier")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public String getCashierView(Model model, Authentication authentication) {
        model.addAttribute("email", authentication.getName());
        model.addAttribute("tableTypes", tableTypeService.findAll());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "cashier";
    }
}
