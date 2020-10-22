package com.adrianstypinski.ytsongimporter;

import com.adrianstypinski.ytsongimporter.exceptions.UserNotFoundException;
import com.adrianstypinski.ytsongimporter.payload.PlaylistTransferRequest;
import lombok.extern.slf4j.Slf4j;
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
    public void updatePlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        transferService.transferToExistingPlaylist(session, requestBody);
    }

    @PostMapping("me/transfer/submit")
    public void createNewPlaylist(HttpSession session, @RequestBody PlaylistTransferRequest requestBody) throws UserNotFoundException {
        transferService.transferToNewPlaylist(session, requestBody);
    }
}
