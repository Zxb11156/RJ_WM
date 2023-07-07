package Zxb.Service.Impl;


import Zxb.Entity.Dish;
import Zxb.Entity.DishFlavor;
import Zxb.Dto.DishDto;
import Zxb.Mapper.DishMapper;
import Zxb.Service.DishFlavorService;
import Zxb.Service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品同时保存口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存基本菜品信息
        this.save(dishDto);
        // 保存菜品口味到dish_flavor表,
        // 菜品口味是一个集合即一个菜品可以有多种口味
        // 例如微辣、中辣、重辣、变态辣等
        // 获取菜品的id 上面保存的时候才会通过雪花算法生成，一开始是没有的
        // 所以这里需要在添加上
        Long id = dishDto.getId();
        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 方法一 遍历
//        for (DishFlavor flavor : flavors) {
//            flavor.setDishId(id);
//        }
        // 方法二 stream流
        flavors = flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }


    //根据id查询菜品信息和对应的口味信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);
        log.info(dish.toString());
        //构造DTO对象
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        log.info(dishDto.toString());
        //查询菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        log.info(flavors.toString());
        //复制口味信息
        dishDto.setFlavors(flavors);
        log.info(dishDto.toString());
        //返回结果对象
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品表信息
        this.updateById(dishDto);
        //更新口味表信息
        //因为口味信息的更新涉及口味的增加或减少
        //所以直接更新是行不通的，这里先删除再添加来实现更新
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //重新添加
        List<DishFlavor> flavors = dishDto.getFlavors();
        //因为是先删除后添加，所以少了一个菜品id。这里把菜品id补上
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
