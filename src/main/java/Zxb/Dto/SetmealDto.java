package Zxb.Dto;


import Zxb.Entity.Setmeal;
import Zxb.Entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
