package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.payload.PlaylistTransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
public class ApiController {

    private final PlaylistsTransferService transferService;

    public ApiController(PlaylistsTransferService transferService) {
        this.transferService = transferService;
    }

    @PutMapping("me/transfer/submit")
    public void updatePlaylist(HttpSession session,
                               Model model,
                               @RequestBody PlaylistTransferRequest requestBody) {
        log.info(requestBody.toString());
    }

    @PostMapping("me/transfer/submit")
    public void createNewPlaylist(HttpSession session,
                                  Model model,
                                  @RequestBody PlaylistTransferRequest requestBody) {
        log.info(requestBody.toString());
    }
}
