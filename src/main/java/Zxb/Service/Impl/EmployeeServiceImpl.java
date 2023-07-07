package Zxb.Service.Impl;


import Zxb.Entity.Employee;
import Zxb.Mapper.EmployeeMapper;
import Zxb.Service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
