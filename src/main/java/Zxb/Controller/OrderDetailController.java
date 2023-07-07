package Zxb.Controller;

import Zxb.Common.R;
import Zxb.Entity.Orders;
import Zxb.Service.OrderDetailService;
import Zxb.Service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;



}
