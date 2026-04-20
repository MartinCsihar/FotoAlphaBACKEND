package com.fotoalpha.awsservice.Events;

import java.util.List;

public record SavePhotosEvent(
        List<String> presignedURLs
) {
}
