package com.adrianstypinski.ytsongimporter.authorization;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.RedirectView;

public interface AuthorizationService {

    RedirectView getAuthorization(ModelMap model);

}
