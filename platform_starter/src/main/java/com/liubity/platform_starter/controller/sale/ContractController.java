package com.liubity.platform_starter.controller.sale;

import com.liubity.platform_starter.annotation.LogOperation;
import com.liubity.platform_starter.model.backstage.Account;
import com.liubity.platform_starter.model.common.*;
import com.liubity.platform_starter.model.sale.Contract;
import com.liubity.platform_starter.model.sale.ContractFile;
import com.liubity.platform_starter.service.sale.ContractFileService;
import com.liubity.platform_starter.service.sale.ContractService;
import com.liubity.platform_starter.utils.CacheUtil;
import com.liubity.platform_starter.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author Liubity
 * @Date 2020-11-26
 */
@RestController
@RequestMapping("/contract")
public class ContractController {
    
    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractFileService fileService;
    
    @Transactional
    @LogOperation("新增合同信息")
    @PostMapping("/save")
    public CommonResponse save(@RequestBody CommonRequest<Contract> request, HttpServletRequest httpServletRequest){
        Contract param=request.getParam();
        Contract byContractName=contractService.findByContractName(param.getContractName());
        if(Objects.nonNull(byContractName)){
            return CommonResponse.build(ReturnCode.ERROR,"合同名称【"+param.getContractName()+"】已经存在");
        }
        String authorization=httpServletRequest.getHeader("Authorization");
        Account account = (Account) CacheUtil.get(authorization);
        param.setOperator(account.getName());
        param.setUploadTime(new Date());
        Contract save=contractService.save(param);
        List<ContractFile> fileList=param.getFileList();
        if(Objects.nonNull(fileList) && fileList.size()>0){
            fileList.forEach(file->{
                file.setContractId(save.getId());
                fileService.save(file);
            });
        }
        return CommonResponse.build(Result.success(),save);
    }
    
    @LogOperation("更改合同信息")
    @Transactional
    @PostMapping("/update")
    public CommonResponse update(@RequestBody CommonRequest<Contract> request){
        Contract param=request.getParam();
        Contract byContractName=contractService.findByContractName(param.getContractName());
        if(Objects.nonNull(byContractName)){
            return CommonResponse.build(ReturnCode.ERROR,"合同名称【"+param.getContractName()+"】已经存在");
        }
        Contract update=contractService.saveAndFlush(param);
        fileService.deleteAllByContractId(update.getId());
        List<ContractFile> fileList=param.getFileList();
        if(Objects.nonNull(fileList) && fileList.size()>0){
            fileList.forEach(file->{
                file.setContractId(update.getId());
                fileService.save(file);
            });
        }
        return CommonResponse.build(Result.success(),update);
    }
    
    @LogOperation("删除合同信息")
    @PostMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable("id") Long id){
        contractService.deleteById(id);
        return CommonResponse.build(Result.success());
    }
    
    @PostMapping("/listPage")
    public CommonResponse listPage(@RequestBody CommonRequest<Contract> request){
        Contract param=request.getParam();
        PageRequest pageRequest=PageHelper.buildPageRequest(request.getPage(), Sort.by(Sort.Direction.DESC,"createTime"));
        Page<Contract> all=contractService.findAll((Specification<Contract>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            //合同称
            if (StringUtil.isNotEmpty(param.getContractName())) {
                predicates.add(criteriaBuilder.like(root.get("contractName"), "%" + param.getContractName() + "%"));
            }
            //上传时间
            if (Objects.nonNull(param.getUploadStartTime())&&Objects.nonNull(param.getUploadEndTime())){
                predicates.add(criteriaBuilder.between(root.get("uploadTime"),param.getUploadStartTime(), param.getUploadEndTime()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageRequest);
        return CommonResponse.build(Result.success(), all);
    }
    
}
