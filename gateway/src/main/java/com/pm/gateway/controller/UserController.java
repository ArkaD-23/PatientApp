package com.pm.gateway.controller;

import com.pm.gateway.dto.*;
import com.pm.userservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @GrpcClient("userService")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    @GetMapping("/{email}/{token}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String email, @PathVariable String token) {

        GetUserRequest req = GetUserRequest.newBuilder()
                .setToken(token)
                .setEmail(email)
                .build();

        ProfileResponse res = userServiceStub.getUser(req);

        System.out.println("getProfile in controller: " + res);

        return ResponseEntity.ok(new ProfileDto(res.getId(), res.getFullname(), res.getEmail(),  res.getRole(), res.getUsername()));
    }

//    @PostMapping("/update")
//    public ResponseEntity<ProfileDto> updateProfile(@RequestBody UpdateDto dto) {
//
//        UpdateRequest req = UpdateRequest.newBuilder()
//                .setId(dto.getId())
//                .setFullname(dto.getFullname())
//                .setEmail(dto.getEmail())
//                .setPassword(dto.getPassword())
//                .setToken(dto.getToken())
//                .build();
//
//        ProfileResponse res  = userServiceStub.update(req);
//
//        return ResponseEntity.ok(new ProfileDto(res.getId(), res.getFullname(), res.getEmail(),  res.getRole(), res.getSuccess(), res.getMessage()));
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<BooleanDto> deleteUser(@RequestBody DeleteUserDto dto) {
        DeleteUserRequest req = DeleteUserRequest.newBuilder()
                .setId(dto.getId())
                .setToken(dto.getToken())
                .build();

        BooleanResponse res = userServiceStub.deleteUser(req);

        return ResponseEntity.ok(new BooleanDto(res.getStatus()));
    }
}
