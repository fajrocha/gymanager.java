package com.faroc.gymanager.common.application.security;

import com.faroc.gymanager.common.application.security.DTOs.CurrentUserDTO;

public interface CurrentUserProvider  {
    CurrentUserDTO getCurrentUser();
}
