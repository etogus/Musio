// Этот класс нужен для того, чтобы поддержать идею обработки ошибок в axios interceptor.

package com.rpo.backend.tools;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Вызов исключения приводит к тому, что сервис возвращает HTTP ответ с кодом 500 (Internal Server Error)
// и в теле ответа сообщение, которое мы можем вывести пользователю в окне приложения
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DataValidationException extends Exception  {

    public DataValidationException(String message){
        super(message);
    }
}
