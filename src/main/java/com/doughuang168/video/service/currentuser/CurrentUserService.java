package com.doughuang168.video.service.currentuser;

import com.doughuang168.video.domain.CurrentUser;

public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}
