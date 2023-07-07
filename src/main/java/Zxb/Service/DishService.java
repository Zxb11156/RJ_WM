package Zxb.Service;

import Zxb.Entity.Dish;
import Zxb.Dto.DishDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    //新增菜品和对应的口味。需要操作两张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //跟新菜品信息同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}
