package com.hyun.demo.auth

import com.hyun.demo.auth.dto.AppUserDTO
import com.hyun.demo.auth.entity.AppUser
import java.time.format.DateTimeFormatter


fun AppUser.toDTO(): AppUserDTO {
    return AppUserDTO(
        email = email,
        createdAt = createdDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    )
}
