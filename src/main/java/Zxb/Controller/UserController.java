package Zxb.Controller;



import Zxb.Common.R;
import Zxb.Entity.User;
import Zxb.Service.UserService;
import Zxb.Utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 移动端发送手机验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        log.info("被调用");
        //获取手机号
        String phone = user.getPhone();
        log.info(phone);
        if (!phone.isEmpty()) {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("验证码为：{}",code);
            //发短信
//            SMSUtils.sendMessage("","",phone,code);
            //保存验证码
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }
        return R.error("发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        //获取手机号
        String phone = map.get("phone").toString();
        log.info(phone);
        //获取验证码
        String code = map.get("code").toString();
        log.info(code);
        //用session中获取保存的验证码
        String codeInSession = String.valueOf(session.getAttribute(phone));
        log.info(codeInSession);
        //比对验证码
        if(codeInSession != null && codeInSession.equals(code)) {
            //对比成功,登陆成功
            //判断是否为新用户，如果是自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            //是新用户
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
