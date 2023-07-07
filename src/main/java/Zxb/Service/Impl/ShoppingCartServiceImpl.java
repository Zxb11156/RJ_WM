package Zxb.Service.Impl;


import Zxb.Entity.ShoppingCart;
import Zxb.Mapper.ShoppingCartMapper;
import Zxb.Service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
