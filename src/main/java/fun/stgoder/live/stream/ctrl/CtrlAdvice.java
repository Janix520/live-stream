package fun.stgoder.live.stream.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import fun.stgoder.live.stream.model.Resp;

@ControllerAdvice
public class CtrlAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionHandler(Exception e, HttpServletRequest request) {
        int code = 500;
        String msg = e.getMessage();
        String uri = request.getRequestURI();
        if (uri.contains("/rest")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(new Resp(code, msg));
        } else {
            ModelAndView mv = new ModelAndView("err");
            mv.addObject("title", code + " - " + msg);
            mv.addObject("code", code);
            mv.addObject("msg", msg);
            return mv;
        }
    }
}
