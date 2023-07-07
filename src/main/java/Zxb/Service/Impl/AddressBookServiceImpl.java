package Zxb.Service.Impl;


import Zxb.Entity.AddressBook;
import Zxb.Mapper.AddressBookMapper;
import Zxb.Service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
