package com.accesshr.emsbackend.EmployeeController;


import com.accesshr.emsbackend.Dto.EmployeeDTO;
import com.accesshr.emsbackend.Dto.LoginDTO;
import com.accesshr.emsbackend.Service.EmployeeService;
import com.accesshr.emsbackend.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/v1/employee")
public class EmployeeController {



    @Autowired
    private EmployeeService employeeService;

    @PostMapping(path = "/save")
//    public String saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
//        String id = employeeService.addEmployee(employeeDTO);
//        return id;
//    }
    public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        if (employeeService.isEmailExists(employeeDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        String id = employeeService.addEmployee(employeeDTO);
        return ResponseEntity.ok(id);
    }

    @PostMapping(path = "/emailExists")
    public ResponseEntity<?> checkEmailExists(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean exists = employeeService.isEmailExists(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginEmployee(@RequestBody LoginDTO loginDTO) {
        LoginResponse loginResponse = employeeService.loginEmployee(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }
}
