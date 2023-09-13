package com.example.commonresult.controller;

import com.example.commonresult.entity.TestDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


/**
 * @author: lichengcan
 * @date: 2023-09-13 18:10
 * @description
 **/
@RestController()
@RequestMapping("/result")
public class ResultController {
    @GetMapping("/{num}")
    public Integer detail(@PathVariable("num") @Min(1) @Max(20) Integer num) {
        int i = 1/0;
        return num * num;
    }

    @GetMapping("/getByEmail")
    public TestDTO getByAccount(@RequestParam @NotBlank @Email String email) {
        TestDTO testDTO = new TestDTO();
        testDTO.setEmail(email);
        return testDTO;
    }


}
