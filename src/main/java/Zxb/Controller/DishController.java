package Zxb.Controller;


import Zxb.Common.R;
import Zxb.Entity.Category;
import Zxb.Entity.Dish;
import Zxb.Entity.DishFlavor;
import Zxb.Dto.DishDto;
import Zxb.Service.CategoryService;
import Zxb.Service.DishFlavorService;
import Zxb.Service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        //菜品的分类涉及多表查询
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //拷贝之后需要添加上菜品分类的属性
        //先拿到所有的菜品
        List<Dish> records = pageInfo.getRecords();
        //根据分类id查询分类名称
        List<DishDto> list = records.stream().map((item) -> {
            //复制单个菜品对象
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            //根据id查名字信息
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //赋值并返回
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /**
     * 根据id查询菜品信息和对应口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        //更新两张表
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }


    /**
     * 根据条件查询菜品数据,在套餐管理添加菜品一栏。
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //等值查询对应分类id下的菜品
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //只查询起售状态的，停售状态（status==0）的不查询
        queryWrapper.eq(Dish::getStatus,1);
        //按照排序优先度从小到大排序，优先度一样的按更新时间的远近排
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        //还需要有菜品的口味信息
        //复制所有菜品为Dto对象
        List<DishDto> dtoList = new ArrayList<>();
        for (Dish dish1 : list) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            dtoList.add(dishDto);
        }
        //查询每一个菜品对应的所有口味
        for (DishDto dishDto : dtoList) {
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dishDto.getId());
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(list1);
        }
        log.info(String.valueOf(dtoList));
        return R.success(dtoList);
    }

    /**
     * 停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> status0 (Long[] ids) {
        log.info(Arrays.toString(ids));
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(0);
            dishService.updateById(dish);
        }
        return R.success("菜品已停售");
    }

    /**
     * 起售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> status1 (Long[] ids) {
        log.info(Arrays.toString(ids));
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(1);
            dishService.updateById(dish);
        }
        return R.success("菜品已起售");
    }


    @DeleteMapping
    public R<String> delete(Long[] ids) {
        log.info(Arrays.toString(ids));
        for (Long id : ids) {
            dishService.removeById(id);
        }
        return R.success("删除成功");
    }
}
