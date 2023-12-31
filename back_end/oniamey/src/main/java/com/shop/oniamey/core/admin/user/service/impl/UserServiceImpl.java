package com.shop.oniamey.core.admin.user.service.impl;

import com.shop.oniamey.core.common.model.request.ChangePasswordRequest;
import com.shop.oniamey.core.admin.user.model.request.ModifyUserRequest;
import com.shop.oniamey.core.admin.user.model.response.UserDetailResponse;
import com.shop.oniamey.core.admin.user.model.response.UserResponse;
import com.shop.oniamey.core.admin.user.service.UserService;
import com.shop.oniamey.entity.Role;
import com.shop.oniamey.entity.User;
import com.shop.oniamey.repository.user.RoleRepository;
import com.shop.oniamey.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserResponse> getAllStaffs(Pageable pageable) {
        return userRepository.getAllUsers(pageable);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.getAllUsers();
    }
    @Override
    public UserDetailResponse getUserById(Long id) {
        return userRepository.getUserDetailById(id);
    }

    @Override
    public String createStaff(ModifyUserRequest modifyUserRequest) {
        Optional<User> checkUser = userRepository.findByEmail(modifyUserRequest.getEmail());
        Optional<Role> role = roleRepository.findById(modifyUserRequest.getRoleId());

        if (checkUser.isPresent()) {
            return "Email already exists";
        }

        if (modifyUserRequest.getPhoneNumber() != null) {
            checkUser = userRepository.findByPhoneNumber(modifyUserRequest.getPhoneNumber());
            if (checkUser.isPresent()) {
                return "Phone number already exists";
            }
        }

        if (role.isEmpty()) {
            return "Role not found";
        }

        User user = new User();
        user.setFullName(modifyUserRequest.getFullName());
        user.setEmail(modifyUserRequest.getEmail());
        user.setPhoneNumber(modifyUserRequest.getPhoneNumber());
        user.setGender(modifyUserRequest.getGender());
        user.setPassword(modifyUserRequest.getPassword());
        user.setAddress(modifyUserRequest.getAddress());
        user.setBirthDate(modifyUserRequest.getBirthDate());
        user.setAvatar(modifyUserRequest.getAvatar());
        user.setRole(role.get());
        user.setDeleted(modifyUserRequest.getIsDeleted());
        userRepository.save(user);
        return "Create staff success";
    }

    @Override
    public String updateStaff(Long id, ModifyUserRequest modifyUserRequest) {
        Optional<User> checkUser = userRepository.findById(id);
        Optional<Role> role = roleRepository.findById(modifyUserRequest.getRoleId());

        if (checkUser.isEmpty()) {
            return "User not found";
        }

        if (role.isEmpty()) {
            return "Role not found";
        }

        User user = checkUser.get();
        user.setFullName(modifyUserRequest.getFullName());
        user.setEmail(modifyUserRequest.getEmail());
        user.setPhoneNumber(modifyUserRequest.getPhoneNumber());
        user.setGender(modifyUserRequest.getGender());
        user.setAddress(modifyUserRequest.getAddress());
        user.setBirthDate(modifyUserRequest.getBirthDate());
        user.setAvatar(modifyUserRequest.getAvatar());
        user.setRole(role.get());
        user.setDeleted(modifyUserRequest.getIsDeleted());
        userRepository.save(user);
        return "Update staff success";
    }

    @Override
    public String changePassword(Long id, ChangePasswordRequest changePasswordRequest) {
        Optional<User> checkUser = userRepository.findById(id);
        if (checkUser.isEmpty()) {
            return "User not found";
        }
        User user = checkUser.get();
        if (!user.getPassword().equals(changePasswordRequest.getOldPassword())) {
            return "Old password is incorrect";
        }
        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
        return "Change password success";
    }

    @Override
    public String updateStatus(Long id) {
        Optional<User> checkUser = userRepository.findById(id);
        if (checkUser.isEmpty()) {
            return "User not found";
        }
        User user = checkUser.get();
        Boolean status = user.getDeleted();
        user.setDeleted(!status);
        userRepository.save(user);
        return "Update status success";
    }

    @Override
    public Long getTotalPage() {
        long totalPage = userRepository.count();
        Long endPage = totalPage / 5;
        if (totalPage % 5 != 0) {
            endPage = endPage + 1;
        }
        System.out.println(endPage);
        return endPage;
    }

}
