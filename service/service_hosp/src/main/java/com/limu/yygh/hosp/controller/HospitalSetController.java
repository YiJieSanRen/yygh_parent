package com.limu.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limu.yygh.common.result.R;
import com.limu.yygh.hosp.service.HospitalSetService;
import com.limu.yygh.hosp.util.MD5;
import com.limu.yygh.model.hosp.HospitalSet;
import com.limu.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author limu
 * @since 2022-07-22
 */
//医院设置接口
@Api(tags = "医院设置接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/hosp/hospital-set")
public class HospitalSetController {

    @Autowired
    public HospitalSetService hospitalSetService;

    //查询所有医院设置
    @ApiOperation("医院设置列表")
    @GetMapping("findAll")
    public R findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return R.ok().data("list", list);
    }

    //根据医院id删除某个医院信息
    @ApiOperation("根据医院id删除某个医院信息")
    @DeleteMapping("/remove/{id}")
    public R deleteById(@ApiParam(name = "id", value = "医院id", required = true) @PathVariable("id") Integer id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag ? R.ok() : R.error();
    }

    @ApiOperation("查询医院分页信息")
    @GetMapping("/{page}/{limit}")
    public R pageList(@PathVariable Integer page, @PathVariable Integer limit) {
        Page<HospitalSet> hospitalSetPage = new Page<>(page, limit);
        hospitalSetService.page(hospitalSetPage);
        return R.ok().data("total", hospitalSetPage.getTotal()).data("rows", hospitalSetPage.getRecords());
    }

    @ApiOperation("根据医院id查询医院详情")
    @GetMapping("/detail")
    public R getHospitalById(@RequestParam("id") Integer id) {
        return R.ok().data("item", hospitalSetService.getById(id));
    }

    @ApiOperation("根据医院id修改医院信息")
    @PostMapping("/updateHospSet")
    public R updateHospSet(@RequestBody HospitalSet hospitalSet) {
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

    //批量删除医院设置
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("batchRemove")
    public R batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return R.ok();
    }

    // 医院设置锁定和解锁
    @ApiOperation("医院设置锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public R lockHospitalSet(@PathVariable Long id,
                             @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

    @ApiOperation("添加医院")
    @PostMapping("/saveHospSet")
    public R save(@RequestBody HospitalSet hospitalSet) {
        hospitalSet.setStatus(0);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        hospitalSetService.save(hospitalSet);
        return R.ok();
    }

    @ApiOperation("分页条件医院设置列表")
    @PostMapping("/{page}/{limit}")
    public R pageQuery(@PathVariable Integer page, @PathVariable Integer limit, @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> hospitalSetPage = new Page<>(page, limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<HospitalSet>();

        if (hospitalSetQueryVo == null) {
            hospitalSetService.page(hospitalSetPage);
        } else {
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();

            if (!StringUtils.isEmpty(hosname)) {
                queryWrapper.like("hosname", hosname);
            }

            if (!StringUtils.isEmpty(hoscode)) {
                queryWrapper.eq("hoscode", hoscode);
            }
            hospitalSetService.page(hospitalSetPage, queryWrapper);
        }
        return R.ok().data("total", hospitalSetPage.getTotal()).data("rows", hospitalSetPage.getRecords());
    }

}

