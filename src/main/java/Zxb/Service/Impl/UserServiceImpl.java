package Zxb.Service.Impl;


import Zxb.Entity.User;
import Zxb.Mapper.UserMapper;
import Zxb.Service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}


