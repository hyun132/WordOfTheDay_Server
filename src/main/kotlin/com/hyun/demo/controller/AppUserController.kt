package com.hyun.demo.controller

import com.hyun.demo.dto.AppUserDTO
import com.hyun.demo.dto.RegisterAppUserDto
import com.hyun.demo.service.AppUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class AppUserController(private val appUserService: AppUserService) {

    @PostMapping("/signup")
    fun register(@RequestBody registerAppUserDto: RegisterAppUserDto): ResponseEntity<AppUserDTO> {
        val user = appUserService.registerUser(registerAppUserDto)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/{email}")
    fun getUser(@PathVariable email: String): ResponseEntity<AppUserDTO> {
        var userId = 1
        val user = appUserService.getUser(email = email)
        return ResponseEntity.ok(user)
    }

}