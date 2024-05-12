package dut.project.pbl3.services;

import dut.project.pbl3.dto.orderDetail.update.UpdatePriceDto;
import dut.project.pbl3.models.OrderDetail;
import dut.project.pbl3.models.User;
import dut.project.pbl3.repositories.OrderDetailRepository;
import dut.project.pbl3.repositories.UserRepository;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    public void cancelItem(Long id, Long userId, String deleteContent) throws NotFoundException {
        Optional<OrderDetail> orderDetail = this.orderDetailRepository.findById(id);
        orderDetail.orElseThrow(NotFoundException::new);

        Optional<User> user = this.userRepository.findById(userId);
        user.orElseThrow(() -> new NotFoundException("Not found user"));

        orderDetail.get().setCanceled(true);
        orderDetail.get().setCancelReason(deleteContent);
        orderDetail.get().setUser(user.get());
        this.orderDetailRepository.save(orderDetail.get());
    }

    public void updatePrice(Long id, UpdatePriceDto updatePriceDto) throws NotFoundException {
        Optional<OrderDetail> orderDetail = this.orderDetailRepository.findById(id);
        orderDetail.orElseThrow(NotFoundException::new);

        orderDetail.get().setPrice(updatePriceDto.getPrice());
        this.orderDetailRepository.save(orderDetail.get());
    }
}
