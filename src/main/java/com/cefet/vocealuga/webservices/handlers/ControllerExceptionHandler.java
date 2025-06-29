package com.cefet.vocealuga.webservices.handlers;

import com.cefet.vocealuga.webservices.exceptions.WebserviceException;
import com.cefet.vocealuga.webservices.responses.MensagemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(WebserviceException.class)
    public ResponseEntity<MensagemResponse> clienteNaoEncontradoException(WebserviceException ex) {
        return ResponseEntity.status(404).body(new MensagemResponse(ex.getMessage(), ex.isBloqueante()));
    }
}
