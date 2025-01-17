package epicode.it.events.utils.upload;

import epicode.it.events.utils.upload.UploadSvc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UploadController {
    private final UploadSvc uploadSvc;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(MultipartFile file) throws Exception {
        return ResponseEntity.ok(uploadSvc.uploadFile(file));
    }
}
