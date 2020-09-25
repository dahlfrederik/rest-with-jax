/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Frederik Dahl <cph-fd76@cphbusiness.dk>
 */
public class ExceptionDTO {

    public ExceptionDTO(int code, String description) {
        this.code = code;
        this.description = description;
    }
    private int code;
    private String description;
}
