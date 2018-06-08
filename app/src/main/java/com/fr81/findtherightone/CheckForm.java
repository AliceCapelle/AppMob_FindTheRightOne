package com.fr81.findtherightone;

/**
 * Created by acapelle on 11/05/2018.
 * Various method to check if entered text is good according to our critters
 */

public class CheckForm {

    /**
     * Check if password containt 8 or more characters
     * @param p password entered by user
     * @return true if password has 8 characters or more, false otherwise
     */
    public static boolean checkPassword(String p){
        return p.length() >= 8;
    }

    /**
     * Check if entered text is college email (with a dot in it)
     * @param e entered email by user
     * @return true if sting contained dot, false otherwise
     */
    public static boolean checkEmail(String e){
        return e.contains(".");
    }



}
