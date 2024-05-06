package com.accesshr.emsbackend.Service.impl;

import com.accesshr.emsbackend.Dto.EmployeeDTO;
import com.accesshr.emsbackend.Dto.LoginDTO;
import com.accesshr.emsbackend.Entity.Employee;
import com.accesshr.emsbackend.Repo.EmployeeRepo;
import com.accesshr.emsbackend.Service.EmployeeService;
//import jdk.internal.icu.impl.Punycode;
import com.accesshr.emsbackend.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeIMPL implements EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String addEmployee(EmployeeDTO employeeDTO) {
        String encodedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        Employee employee = new Employee(
                employeeDTO.getEmployeeid(),
                employeeDTO.getEmployeename(),
                employeeDTO.getEmail(),
                employeeDTO.getCompanyname(),
                encodedPassword
        );
        employeeRepo.save(employee);
        return employee.getEmployeename();
    }

    @Override
    public boolean isEmailExists(String email) {
        Employee existingEmployee = employeeRepo.findByEmail(email);
        return existingEmployee != null;
    }

    @Override
    public LoginResponse loginEmployee(LoginDTO loginDTO) {

        String msg = "";
        Employee employee1 = employeeRepo.findByEmail(loginDTO.getEmail());
        if (employee1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = employee1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<Employee> employee = employeeRepo.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (employee.isPresent()) {
                    return new LoginResponse("Login Success", true);
                } else {
                    return new LoginResponse("Login Failed", false);
                }
            } else {
                return new LoginResponse("password Not Match", false);
            }
        }else {
            return new LoginResponse("Email not exits", false);
        }
    }
}
