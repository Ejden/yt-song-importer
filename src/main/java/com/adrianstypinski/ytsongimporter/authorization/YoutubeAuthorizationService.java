package com.adrianstypinski.ytsongimporter.authorization;

import com.adrianstypinski.ytsongimporter.Scope;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;

import static com.adrianstypinski.ytsongimporter.Scope.*;
import static com.adrianstypinski.ytsongimporter.Scope.USER_READ_PRIVATE;

@Service
public class YoutubeAuthorizationService implements AuthorizationService {

    public RedirectView getAuthorization(ModelMap model) {
        return null;
    }
}
