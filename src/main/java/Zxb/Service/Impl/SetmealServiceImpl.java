package Zxb.Service.Impl;


import Zxb.Common.CustomException;
import Zxb.Entity.Setmeal;
import Zxb.Entity.SetmealDish;
import Zxb.Dto.SetmealDto;
import Zxb.Mapper.SetmealMapper;
import Zxb.Service.SetmealDishService;
import Zxb.Service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，设计两张表的新增操作。套餐表和套餐菜品表
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存基本信息，即套餐表
        this.save(setmealDto);
        //保存套餐菜品信息，即套餐菜品表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //传参的时候套餐表未添加所以没有封装套餐id。套餐id是添加的时候雪花算法自动生成的
        //这里还需要吧上面保存后生成的id添加上
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 删除套餐，同时删除套餐表和套餐菜品表
     * 只能删除已经停售的套餐，起售状态的套餐正在售价中不能直接删除
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        //不能删除就抛出业务异常
        if (count > 0) {
            throw new CustomException("该套餐正在售卖中，不能删除");
        }
        //可以删除，先删除套餐表的数据
        this.removeByIds(ids);
        //再删除关联表也就是套餐菜品表的数据。通过关连键，也就是套餐id（Setmeal_id）进行删除
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }


    /**
     * 通过id查询套餐和套餐包含菜品的信息
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //创建对象
        SetmealDto setmealDto = new SetmealDto();
        //查询套餐信息
        Setmeal setmeal = this.getById(id);
        //查询套餐包含的菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     * 更新套餐信息，两张表
     * @param setmealDto
     */
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //更新套餐基本信息
        this.updateById(setmealDto);
        //更新套餐菜品信息
        //菜品信息可能涉及增加或删除，不能直接更新
        //需要先删除在重新添加
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //重新添加
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //先删除后添加导致这里缺少套餐id，因为dto里面没有这个属性的信息，所以需要在重新设置一下
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);

    }


}
