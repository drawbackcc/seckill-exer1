package com.xxx.controller;

import com.xxx.dto.Exposer;
import com.xxx.dto.SeckillExecution;
import com.xxx.dto.SeckillResult;
import com.xxx.entity.Seckill;
import com.xxx.enums.SeckillStatEnum;
import com.xxx.exception.RepeatKillException;
import com.xxx.exception.SeckillCloseException;
import com.xxx.exception.SeckillException;
import com.xxx.service.interfaces.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: czm, PC of Chenzhimei
 * @time: 2020/10/27 0:25
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
//    @Autowired
//    private SeckillService seckillService;
    //@Autowired 可以对成员变量、方法以及构造函数进行注释，推荐对构造函数进行注释，明确成员变量的加载顺序。
    //但这里都一样
    private final SeckillService seckillService;

    @Autowired
    public SeckillController(SeckillService seckillService){
        this.seckillService = seckillService;
    }

    /**
     *
     * @description:获取秒杀商品列表
     * @time:        2020/10/27 0:50
     */
    @RequestMapping({"/list","/","/index","index.html"})
    public String list(Model model){
        model.addAttribute("list", seckillService.getSeckillList());
        return "list";
    }

    @ResponseBody
    @RequestMapping("/listJson")
    public List<Seckill> listJson(){
        return seckillService.getSeckillList();
    }

    @RequestMapping("/{seckillId}/detail")
    public String detail(@PathVariable("seckillId")Long seckillId, Model model){
        Seckill seckill;
        if(seckillId == null || (seckill = seckillService.getById(seckillId)) == null) {
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @ResponseBody
    @RequestMapping("/{seckillId}/detailJson")
    public Seckill detailJson(@PathVariable("seckillId")Long seckillId){
        if(seckillId == null) {
            return null;
        }
        return seckillService.getById(seckillId);
    }

    @RequestMapping("/{seckillId}/exposer")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        }catch (Exception e){
            e.printStackTrace();
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST)
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
                                                   @PathVariable("md5")String md5,
                                                   @CookieValue(value = "userPhone", required = false) Long userPhone){
        if(userPhone == null){
            return new SeckillResult<>(false, "没有注册");
        }
        SeckillExecution execution = null;
        try {
            execution = seckillService.executeSeckill(seckillId, userPhone, md5);
//            execution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            return new SeckillResult<>(true, execution);
        }catch (RepeatKillException e){
            execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(false, execution);
        }catch (SeckillCloseException e){
            execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<>(false, execution);
        }catch (SeckillException e){
            execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(false, execution);
        }
    }

    /**
     * 获取服务器端时间,防止用户篡改客户端时间提前参与秒杀
     *
     * @return 时间的json数据
     */
    @RequestMapping("/time/now")
    @ResponseBody
    public SeckillResult<LocalDateTime> time() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new SeckillResult<>(true, localDateTime);
    }

//    @ResponseBody
    @RequestMapping("/setCookie")
    public String setPhoneCookie(String name,String value, HttpServletResponse response, Model model){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(30 * 60);// 设置为30min
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/seckill/showCookies";
    }

    @ResponseBody
    @RequestMapping("/showCookies")
    public Map<String, Object> showCookies(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
        result.put("cookies", cookies);
        return result;
    }


}
