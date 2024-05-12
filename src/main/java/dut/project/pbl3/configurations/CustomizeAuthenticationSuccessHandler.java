package dut.project.pbl3.configurations;

import com.google.gson.Gson;
import dut.project.pbl3.utils.httpResponse.okResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dut.project.pbl3.common.enums.RoleEnum.*;

@Configuration
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        String json = "";
        GrantedAuthority role = authentication.getAuthorities().iterator().next();
        if ((ADMIN.name()).equals(role.getAuthority())) {
            json = new Gson().toJson(new okResponse("/admin"));
        } else if ((STAFF.name()).equals(role.getAuthority())) {
            json = new Gson().toJson(new okResponse("/cashier"));
        }
        httpServletResponse.getWriter().write(json);
    }
}
