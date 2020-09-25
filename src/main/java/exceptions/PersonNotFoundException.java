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
public class PersonNotFoundException extends Exception {
    public PersonNotFoundException(String message) {
        super(message);
    }
}

