package com.example.usefulapplication.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InputValidation {

    public static boolean dateIsValid(String date){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date, formatter);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean inputIsEmpty(String...input){
        for (String text: input) {
            if (text.trim().length() == 0){
                return true;
            }
        }

        return false;
    }
}
