package dut.project.pbl3.services;

import dut.project.pbl3.dto.orderDetail.AddItemDto;
import dut.project.pbl3.dto.orderDetail.update.UpdateItemDto;
import dut.project.pbl3.dto.orderDetail.update.UpdateOrderDetailDto;
import dut.project.pbl3.dto.tableOrder.*;
import dut.project.pbl3.dto.tableOrder.splitBill.SplitDto;
import dut.project.pbl3.dto.tableOrder.splitBill.SplitedProductsDto;
import dut.project.pbl3.models.*;
import dut.project.pbl3.repositories.*;
import dut.project.pbl3.utils.HelperUtil;
import dut.project.pbl3.utils.ObjectMapperUtils;
import dut.project.pbl3.utils.httpResponse.exceptions.BadRequestException;
import dut.project.pbl3.utils.httpResponse.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class TableOrderService {
    private final TableOrdersRepository tableOrdersRepository;
    private final TableRepository tableRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public GetTableOrderDto createOrder(Long tableId) throws NotFoundException, BadRequestException {
        Optional<Table> table = this.tableRepository.findById(tableId);
        table.orElseThrow(() -> new NotFoundException("Not found table Id"));

        Optional<TableOrder> activeTableOrder = table.get().getTableOrders()
                .stream()
                .filter(tableOrder -> !tableOrder.isCanceled && tableOrder.getEndAt() == null)
                .findFirst();

        if(activeTableOrder.isPresent()){
            throw new BadRequestException("Stop the time on bill Id (" + activeTableOrder.get().getId() + ") first!");
        }

        double tablePrice = table.get().getTableType().getPrice();

        TableOrder tableOrder = new TableOrder();
        tableOrder.setTable(table.get());
        tableOrder.setTablePrice(tablePrice);
        TableOrder createdOrder = this.tableOrdersRepository.saveAndFlush(tableOrder);
        table.get().setStatus(true);
        this.tableRepository.save(table.get());
        return ObjectMapperUtils.map(createdOrder, GetTableOrderDto.class);
    }

    public void deleteOne(Long id) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findById(id);
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Bill"));

        tableOrder.get().getOrderDetails().forEach(item -> {
            item.setTableOrder(null);
            this.orderDetailRepository.save(item);
        });

        this.tableOrdersRepository.delete(tableOrder.get());
    }

    public void restoreOne(Long id) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findById(id);
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Bill"));

        tableOrder.get().setPaid(false);
        tableOrder.get().setCanceled(false);
        tableOrder.get().setCancelReason(null);

        tableOrder.get().getTable().setStatus(true);

        this.tableOrdersRepository.save(tableOrder.get());
    }

    public List<GetTableOrderDto> findActives() {
        List<TableOrder> tableOrders = this.tableOrdersRepository.findByIsCanceledFalseAndIsPaidFalse();
        List<OrderDetail> canceledOrderDetails = new ArrayList<>();
        tableOrders.forEach(bill -> {
            bill.getOrderDetails().forEach(item -> {
                if (item.isCanceled())
                    canceledOrderDetails.add(item);
            });
            bill.getOrderDetails().removeAll(canceledOrderDetails);
        });
        return ObjectMapperUtils.mapAll(tableOrders, GetTableOrderDto.class);
    }

    public List<GetTableOrderDto> findFinished() {
        return ObjectMapperUtils.mapAll(this.tableOrdersRepository.findAllByDeletedAtIsNullAndIsPaidIsTrueOrIsCanceledIsTrue(), GetTableOrderDto.class);
    }

    public StatisticsDto getStatistic(){
        LocalDateTime today = LocalDateTime.now();
        List<TableOrder> foundTables = this.tableOrdersRepository.findAllOrderByBeginAtAndIsPaid(today.toLocalDate().toString());
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setTotalAmount(foundTables.stream().mapToDouble(tableOrder -> tableOrder.total).sum());
        statisticsDto.setNumberOfOrder(foundTables.size());
        return statisticsDto;
    }

    public GetTableOrderDto findActiveById(Long id) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(id);
        tableOrder.orElseThrow(NotFoundException::new);

        List<OrderDetail> falseOrderDetails = new ArrayList<>();
        tableOrder.get().getOrderDetails().forEach(item -> {
            if (item.isCanceled())
                falseOrderDetails.add(item);
        });

        tableOrder.get().getOrderDetails().removeAll(falseOrderDetails);

        return ObjectMapperUtils.map(tableOrder.get(), GetTableOrderDto.class);
    }

    public Timestamp stopTime(Long tableOrderId) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findById(tableOrderId);
        tableOrder.orElseThrow(NotFoundException::new);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        tableOrder.get().setEndAt(timestamp);
        this.tableOrdersRepository.save(tableOrder.get());
        return timestamp;
    }

    public void continueTime(Long tableOrderId) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findById(tableOrderId);
        tableOrder.orElseThrow(NotFoundException::new);

        tableOrder.get().setEndAt(null);
        this.tableOrdersRepository.save(tableOrder.get());
    }

    public void addItem(AddItemDto addItemDto) throws NotFoundException, BadRequestException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(addItemDto.getTableOrderId());
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Table Order"));

        Optional<Product> product = this.productRepository.findById(addItemDto.getProductId());
        product.orElseThrow(() -> new NotFoundException("Not found Product"));

        if (product.get().getDeletedAt() != null) throw new BadRequestException("Product is not available");

        Optional<OrderDetail> orderDetail = this.orderDetailRepository.findExistItem(addItemDto.getProductId(), addItemDto.getTableOrderId());

        if (orderDetail.isPresent()) {
            double currentAmount = orderDetail.get().getAmount();
            orderDetail.get().setAmount(currentAmount + 1);
            this.orderDetailRepository.save(orderDetail.get());
            return;
        }
        OrderDetail newOrderDetail = new OrderDetail();
        newOrderDetail.setTableOrder(tableOrder.get());
        newOrderDetail.setProduct(product.get());
        newOrderDetail.setAmount(1);
        newOrderDetail.setPrice(product.get().getPrice());
        tableOrder.get().getOrderDetails().add(newOrderDetail);

        this.orderDetailRepository.save(newOrderDetail);
        this.tableOrdersRepository.save(tableOrder.get());
    }

    public void updateItem(Long id, UpdateItemDto updateItemDto) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findById(id);
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Table Order Id"));

        List<UpdateOrderDetailDto> updateOrderDetailDtos = updateItemDto.getOrderDetails();

        updateOrderDetailDtos.forEach(item -> {
            try {
                this.orderDetailRepository.updateOne(item.getId(), item.getPrice(), item.getAmount(), item.isCanceled(), item.getCancelReason());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });

        this.tableOrdersRepository.save(tableOrder.get());
    }

    public void updateTimePrice(Long id, UpdateTimePriceDto updateTimePriceDto) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findById(id);
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Table Order Id"));

        tableOrder.get().setTablePrice(updateTimePriceDto.getTimePrice());

        this.tableOrdersRepository.save(tableOrder.get());
    }

    public void cancelOne(Long id, Long userId, String reason) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(id);
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Table Order"));

        Optional<User> user = this.userRepository.findById(userId);
        user.orElseThrow(() -> new NotFoundException("Not found user"));

        tableOrder.get().setUser(user.get());
        tableOrder.get().setCanceled(true);
        tableOrder.get().setCancelReason(reason);
        this.tableOrdersRepository.save(tableOrder.get());

        List<TableOrder> tableOrdersByTable = this.tableOrdersRepository.findByTableAndIsCanceledFalseAndIsPaidFalse(tableOrder.get().getTable());
        if (tableOrdersByTable.isEmpty()) {
            tableOrder.get().getTable().setStatus(false);
            this.tableRepository.save(tableOrder.get().getTable());
        }
    }

    public void mergeBills(MergeDto mergeDto) throws NotFoundException {
        Set<Long> tableIds = new HashSet<>();

        Optional<TableOrder> fromTableOrder = this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(mergeDto.getFromBill());
        fromTableOrder.orElseThrow(() -> new NotFoundException("Not found bill"));

        tableIds.add(fromTableOrder.get().getTable().getId());

        Optional<TableOrder> toTableOrders = this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(mergeDto.getToBill().get(0));
        toTableOrders.orElseThrow(() -> new NotFoundException("Not found bill"));

        this.orderDetailRepository.setTableOrder(toTableOrders.get().getId(), fromTableOrder.get().getId());

        for (int i = 1; i < mergeDto.getToBill().size(); i++) {
            this.orderDetailRepository.setTableOrder(toTableOrders.get().getId(), mergeDto.getToBill().get(i));
            this.tableOrdersRepository.deleteTableOrderById(mergeDto.getToBill().get(i));
        }

        this.tableOrdersRepository.deleteTableOrderById(fromTableOrder.get().getId());

        this.setFreeStatus(tableIds);
    }

    public void splitBill(SplitDto splitDto) throws NotFoundException {
        Optional<TableOrder> fromTableOrder =  this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(splitDto.getFromTableOrderId());
        fromTableOrder.orElseThrow(() -> new NotFoundException("Not found Bill with ID" + splitDto.getFromTableOrderId()));

        Optional<TableOrder> toTableOrder =  this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(splitDto.getToTableOrderId());
        toTableOrder.orElseThrow(() -> new NotFoundException("Not found Bill with ID" + splitDto.getToTableOrderId()));

        //get list of product to check if that product already contained in toBill
        List<Long> toTableOrderProductIds = new ArrayList<>();
        toTableOrder.get().getOrderDetails().forEach(orderDetail ->
                toTableOrderProductIds.add(orderDetail.getProduct().getId())
        );

        //loop through fromTableOrder and splitProducts to check what orderDetail needs to split
        fromTableOrder.get().getOrderDetails().forEach(fromTableOrderDetail ->
                splitDto.getProducts().forEach(product -> {
                    //if matched
                    if(fromTableOrderDetail.getId() == product.getOrderDetailId() && product.getSplitAmount() > 0 && !fromTableOrderDetail.isCanceled()){
                        //if the split amount < orderDetail in fromTableOrder amount
                        if(product.getSplitAmount() < fromTableOrderDetail.getAmount()){
                            //save change to toTableOrder after split
                            saveChangesAfterSplit(toTableOrder.get(), toTableOrderProductIds, fromTableOrderDetail, product);
                            //calculate and set the remain amount of orderDetail in fromTableOrder
                            double remainAmount =  fromTableOrderDetail.getAmount() - product.getSplitAmount();
                            fromTableOrderDetail.setAmount(remainAmount);
                            this.orderDetailRepository.save(fromTableOrderDetail);
                        }
                        //if the split amount == orderDetail in fromTableOrder amount
                        else if(fromTableOrderDetail.getAmount() == product.getSplitAmount()){
                            saveChangesAfterSplit(toTableOrder.get(), toTableOrderProductIds, fromTableOrderDetail, product);
                            this.orderDetailRepository.delete(fromTableOrderDetail);
                        }
                }
        }));
    }

    public void transferBill(TransferDto transferDto) throws NotFoundException {
        Set<Long> tableIds = new HashSet<>();
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(transferDto.getBillId());
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Bill"));

        Optional<Table> table = this.tableRepository.findById(transferDto.getTableId());
        table.orElseThrow(() -> new NotFoundException("Not found Table"));

        table.get().setStatus(true);
        this.tableRepository.save(table.get());
        tableIds.add(tableOrder.get().getTable().getId());

        tableOrder.get().setTable(table.get());
        this.tableOrdersRepository.save(tableOrder.get());

        setFreeStatus(tableIds);
    }

    public void setFreeStatus(Set<Long> tableIds) {
        tableIds.forEach(tableId -> {
            List<TableOrder> tableOrders = this.tableOrdersRepository.findByTableIdAndIsCanceledFalseAndIsPaidFalse(tableId);
            if (tableOrders.isEmpty()) this.tableRepository.setFreeStatus(tableId);
        });
    }

    public void payBill(Long id, PayTableOrderDto payTableOrderDto) throws NotFoundException {
        Optional<TableOrder> tableOrder = this.tableOrdersRepository.findByIdAndIsCanceledFalseAndIsPaidFalse(id);
        tableOrder.orElseThrow(() -> new NotFoundException("Not found Bill"));

        double totalPrice = getTotalBill(tableOrder.get());

        tableOrder.get().setTotal(totalPrice);
        tableOrder.get().setPaid(true);

        tableOrder.get().setDiscount(payTableOrderDto.getDiscount());
        this.tableOrdersRepository.save(tableOrder.get());

        setFreeStatus(new HashSet<>(Collections.singletonList(tableOrder.get().getTable().getId())));
    }

    private double getTotalBill(TableOrder tableOrder) {
        double total = 0;

        double diffHour = HelperUtil.getDiffHour(tableOrder.getBeginAt(), tableOrder.getEndAt());

        // time price
        total += tableOrder.getTablePrice() * diffHour;

        total += tableOrder.getOrderDetails().stream().mapToDouble(item -> {
            double cur = item.getProduct().getQuantityRemain();
            item.getProduct().setQuantityRemain(cur - item.getAmount());
            return item.getAmount() * item.getPrice();
        }).sum();


        return total - tableOrder.getDiscount();
    }

    private void saveChangesAfterSplit(TableOrder toTableOrder,
                                       List<Long> toTableOrderProductIds,
                                       OrderDetail fromTableOrderDetail,
                                       SplitedProductsDto splitedProductsDto) {
        //check if the product split from "fromTableOrderDetail" already contained in toTableOrder
        if(!toTableOrderProductIds.isEmpty() && toTableOrderProductIds.contains(fromTableOrderDetail.getProduct().getId())){
            //get the orderDetail from the toOrderTable to change its amount
            Optional<OrderDetail> optionalOrderDetail = toTableOrder.getOrderDetails()
                    .stream()
                    .filter(eachOrderDetail -> eachOrderDetail.getProduct().getId() == fromTableOrderDetail.getProduct().getId())
                    .findFirst();
            if(optionalOrderDetail.isPresent()){
                //calculate and set the amount of toTableOrder's orderDetail after split
                double currentAmount = optionalOrderDetail.get().getAmount();
                optionalOrderDetail.get().setAmount(currentAmount + splitedProductsDto.getSplitAmount());
                this.orderDetailRepository.save(optionalOrderDetail.get());
            }
            return;
        }
        //if not contain, create new orderDetail based on the product and add into toTableOrder
        OrderDetail splitOrderDetail = new OrderDetail();
        splitOrderDetail.setProduct(fromTableOrderDetail.getProduct());
        splitOrderDetail.setAmount(splitedProductsDto.getSplitAmount());
        splitOrderDetail.setPrice(fromTableOrderDetail.getPrice());
        splitOrderDetail.setTableOrder(toTableOrder);
        toTableOrder.getOrderDetails().add(splitOrderDetail);
        this.orderDetailRepository.save(splitOrderDetail);
        this.tableOrdersRepository.save(toTableOrder);
    }
}

