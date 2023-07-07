package Zxb.Service.Impl;


import Zxb.Common.CustomException;
import Zxb.Entity.Category;
import Zxb.Entity.Dish;
import Zxb.Entity.Setmeal;
import Zxb.Mapper.CategoryMapper;
import Zxb.Service.CategoryService;
import Zxb.Service.DishService;
import Zxb.Service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除前需要判断
     * 当前分类没有菜品或套菜才可删除
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联的有菜品，若有，抛出业务异常
        //即查询菜品表中是否有该分类id的菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //判断
        if (count1 > 0) {
            //抛出异常
            throw new CustomException("当前分类下有关联菜品，不能删除");
        }
        //查询当前分类是否关联的有菜品，若有，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //抛出异常
            throw new CustomException("当前分类下有关联套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}
