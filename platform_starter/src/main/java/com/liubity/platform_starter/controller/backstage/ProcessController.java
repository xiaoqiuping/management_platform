package com.liubity.platform_starter.controller.backstage;

import com.liubity.platform_starter.model.backstage.Process;
import com.liubity.platform_starter.model.common.CommonRequest;
import com.liubity.platform_starter.model.common.CommonResponse;
import com.liubity.platform_starter.model.common.PageHelper;
import com.liubity.platform_starter.model.common.Result;
import com.liubity.platform_starter.service.backstage.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Liubity
 * @Date 2020-11-29
 */
@RestController
@RequestMapping("/process")
public class ProcessController {
    
    @Autowired
    private ProcessService service;
    
    @PostMapping("/save")
    private CommonResponse save(@RequestBody CommonRequest<Process> request){
        Process param=request.getParam();
        Process save=service.save(param);
        return CommonResponse.build(Result.success(),save);
    }
    
    @PostMapping("/update")
    private CommonResponse update(@RequestBody CommonRequest<Process> request){
        Process param=request.getParam();
        Process update=service.saveAndFlush(param);
        return CommonResponse.build(Result.success(),update);
    }
    
    @PostMapping("/delete/{id}")
    private CommonResponse delete(@PathVariable("id") Long id){
        service.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    private CommonResponse listPage(@RequestBody CommonRequest<Process> request){
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<Process> all=service.findAll(pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
    @PostMapping("/listAll")
    private CommonResponse listAll(){
        List<Process> all=service.findAll();
        return CommonResponse.build(Result.success(), all);
    }
    
}
