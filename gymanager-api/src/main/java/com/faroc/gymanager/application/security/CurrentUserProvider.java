package com.faroc.gymanager.application.security;

import com.faroc.gymanager.application.security.DTOs.CurrentUserDTO;

public interface CurrentUserProvider  {
    CurrentUserDTO getCurrentUser();
}
