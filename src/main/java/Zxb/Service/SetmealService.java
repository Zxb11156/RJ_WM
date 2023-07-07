package Zxb.Service;

import Zxb.Entity.Setmeal;
import Zxb.Dto.SetmealDto;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，设计两张表的新增操作。套餐表和套餐信息表
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐表和套餐信息表
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 查询套餐信息，套餐信息包括套餐本身和菜品的信息
     * @return
     */
    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);
}
